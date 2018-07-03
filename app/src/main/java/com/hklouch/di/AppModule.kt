package com.hklouch.di

import android.app.Application
import android.content.Context
import com.hklouch.ui.browse.RepoListActivity
import com.hklouch.ui.browse.ReposListFragment
import com.hklouch.ui.search.SearchActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract interface AppModule {

    @Binds
    abstract fun bindContext(application: Application): Context

    @ContributesAndroidInjector
    abstract fun contributesRepoListActivity(): RepoListActivity

    @ContributesAndroidInjector
    abstract fun contributesSearchActivity(): SearchActivity

    @ContributesAndroidInjector
    abstract fun contributesReposListFragment(): ReposListFragment

}