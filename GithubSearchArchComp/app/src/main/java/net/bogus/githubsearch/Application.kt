package net.bogus.githubsearch

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import net.bogus.githubsearch.di.AppComponent
import net.bogus.githubsearch.di.AppModule
import net.bogus.githubsearch.di.DaggerAppComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

/**
 * Created by burak on 11/19/17.
 */
class Application : Application(), HasActivityInjector {

    @Inject lateinit var activityInjector:DispatchingAndroidInjector<Activity>

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return activityInjector
    }

}