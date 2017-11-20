package net.bogus.githubsearch.di

import dagger.Reusable
import dagger.Subcomponent
import dagger.android.AndroidInjector
import net.bogus.githubsearch.MainActivity

/**
 * Created by burak on 11/19/17.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(MainActivityModule::class))
interface MainActivityComponent : AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder:AndroidInjector.Builder<MainActivity>()
}