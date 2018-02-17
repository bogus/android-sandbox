package net.bogus.githubsearch.di

import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Created by burak on 11/19/17.
 */
@Scope
@Retention(value = AnnotationRetention.RUNTIME)
@Qualifier
annotation class ActivityScope