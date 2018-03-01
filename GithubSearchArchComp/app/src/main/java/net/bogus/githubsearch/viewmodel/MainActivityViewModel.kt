package net.bogus.githubsearch.viewmodel

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import net.bogus.githubsearch.APIClient
import net.bogus.githubsearch.model.Repository
import net.bogus.githubsearch.util.ExponentialBackoffRetry
import java.util.concurrent.TimeUnit


open class MainActivityViewModel : ViewModel() {

    var apiClient:APIClient? = null
    set(value) {
        field = value
        isLoading.set(false)
        lastPage.subscribe({
            value ->
            currentPage.set(value)
            makeAPICall(value)
        })
    }

    var result = BehaviorSubject.create<List<Repository>>()
    var error = PublishSubject.create<String>()
    val lastPage = PublishSubject.create<Int>()

    open var isLoading = ObservableField<Boolean>()
    var currentPage = ObservableField<Int>()

    private var lastQuery = ""
    private var compositeDisposable = CompositeDisposable()
    private var data:ArrayList<Repository> = ArrayList()

    fun search(query:String) {
        lastQuery = query
        lastPage.onNext(1)
        data = ArrayList()
        result.onNext(data)
    }

    open fun nextPage() {
        if (isLoading.get()) {
            return
        }
        lastPage.onNext(currentPage.get() + 1)
    }

    private fun makeAPICall(page:Int) {
        isLoading.set(true)
        apiClient?.let {
            val disposable = it.search(lastQuery, page)
                    .timeout(5, TimeUnit.SECONDS)
                    .retryWhen(ExponentialBackoffRetry(5, 3000))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally {
                        isLoading.set(false)
                    }
                    .subscribe(
                            { repositoryResponse ->
                                data.addAll(repositoryResponse.items)
                                result.onNext(data)
                            },
                            { apiError ->
                                error.onNext(apiError.message)
                            }
                    )
            compositeDisposable.add(disposable)
        }
    }

    fun refresh() {
        search(lastQuery)
    }

    open fun shouldLoadNextPage(childCount:Int, totalItemCount:Int, firstVisibleItemPosition:Int) : Boolean {
        if (isLoading.get() == false &&
                childCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= 3) {
            nextPage()
            return true
        }
        return false
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}