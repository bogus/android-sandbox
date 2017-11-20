package net.bogus.githubsearch.viewmodel


import android.databinding.Bindable
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import net.bogus.githubsearch.APIClient
import net.bogus.githubsearch.Application
import net.bogus.githubsearch.di.AppComponent
import net.bogus.githubsearch.di.AppModule
import net.bogus.githubsearch.event.Event
import net.bogus.githubsearch.model.Repository
import javax.inject.Inject

/**
 * Created by burak on 11/19/17.
 */

class MainViewModel {

    var apiClient:APIClient
    var searchDisposable:CompositeDisposable? = null

    var result = PublishSubject.create<List<Repository>>()
    var error = PublishSubject.create<String>()

    @Inject
    constructor(apiClient: APIClient) {
        this.apiClient = apiClient
    }

    fun search(query:String) {
        apiClient.search(query).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            this.result.onNext(result)
                            error = null
                        },
                        {
                            error ->
                            this.error.onNext(error.message)
                        }
                )
    }


}