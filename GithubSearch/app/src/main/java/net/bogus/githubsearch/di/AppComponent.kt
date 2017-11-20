package net.bogus.githubsearch.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import net.bogus.githubsearch.Application
import net.bogus.githubsearch.MainActivity
import net.bogus.githubsearch.viewmodel.MainViewModel
import javax.inject.Singleton

/**
 * Created by burak on 11/19/17.
 */
@Singleton
@Component(modules = arrayOf(
    AndroidInjectionModule::class,
    AppModule::class, ActivityBuilder::class
))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance fun application(application:Application) : Builder
        fun build() : AppComponent
    }

    fun inject(app:Application)
}