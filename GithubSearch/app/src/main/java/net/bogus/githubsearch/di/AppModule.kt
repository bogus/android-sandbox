package net.bogus.githubsearch.di

import android.content.Context
import dagger.Module
import dagger.Provides
import net.bogus.githubsearch.APIClient
import net.bogus.githubsearch.Application
import net.bogus.githubsearch.viewmodel.MainViewModel
import javax.inject.Singleton

/**
 * Created by burak on 11/19/17.
 */
@Module(subcomponents = arrayOf(MainActivityComponent::class))
class AppModule() {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideAPIClient() : APIClient {
        return APIClient.createAPIClient()
    }

}