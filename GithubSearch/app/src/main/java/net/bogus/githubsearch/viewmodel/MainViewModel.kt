package net.bogus.githubsearch.viewmodel


import android.databinding.Bindable
import android.databinding.BindingAdapter
import android.databinding.Observable
import android.databinding.ObservableField
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import net.bogus.githubsearch.APIClient
import net.bogus.githubsearch.Application
import net.bogus.githubsearch.di.AppComponent
import net.bogus.githubsearch.di.AppModule
import net.bogus.githubsearch.event.Event
import net.bogus.githubsearch.model.Repository
import net.bogus.githubsearch.util.ExponentialBackoffRetry
import javax.inject.Inject

/**
 * Created by burak on 11/19/17.
 */

class MainViewModel {

    var apiClient:APIClient
    var searchDisposable:CompositeDisposable? = null

    var result = PublishSubject.create<List<Repository>>()
    var error = PublishSubject.create<String>()
    val lastPage = PublishSubject.create<Int>()

    var isLoading = ObservableField<Boolean>()
    var currentPage = ObservableField<Int>()

    private var lastQuery = ""

    @Inject
    constructor(apiClient: APIClient) {
        this.apiClient = apiClient
        isLoading.set(false)
        // whenever page updates, then make an API call
        lastPage.subscribe { result ->
            currentPage.set(result)
            makeAPICall(result)
        }
    }

    fun search(query:String) {
        result.onNext(ArrayList())
        lastQuery = query
        lastPage.onNext(1)
    }

    fun nextPage() {
        if (isLoading.get()) {
            return
        }
        lastPage.onNext(currentPage.get() + 1)
    }

    private fun makeAPICall(page:Int) {
        isLoading.set(true)
        apiClient.search(lastQuery, page).subscribeOn(Schedulers.io())
                .retryWhen(ExponentialBackoffRetry(3, 3000))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    isLoading.set(false)
                }
                .subscribe(
                        { result ->

                            this.result.onNext(result.items)
                        },
                        {
                            error ->
                            this.error.onNext(error.message)
                        }
                )
    }

    fun refresh() {
        search(lastQuery)
    }

}