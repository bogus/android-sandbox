package net.bogus.githubsearch.di

import dagger.Module
import dagger.Provides
import net.bogus.githubsearch.APIClient
import net.bogus.githubsearch.viewmodel.MainViewModel
import javax.inject.Singleton

/**
 * Created by burak on 11/19/17.
 */
@Module
class MainActivityModule {

    @ActivityScope
    fun provideMainViewModel(apiClient: APIClient) : MainViewModel {
        return MainViewModel(apiClient)
    }
}