package com.hklouch.di

import android.app.Application
import android.content.Context
import com.hklouch.domain.interactor.browse.GetProjectsUseCase
import com.hklouch.domain.interactor.browse.GetProjectsUseCase.Params
import com.hklouch.domain.model.Project
import com.hklouch.ui.ResourceListViewModelFactory
import com.hklouch.ui.browse.BrowseFragment
import com.hklouch.ui.browse.BrowseProjectsActivity
import com.hklouch.ui.detail.ProjectDetailActivity
import com.hklouch.ui.model.UiProjectPreviewItem
import com.hklouch.ui.model.toUiProjectPreviewItem
import com.hklouch.ui.search.SearchActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppModule {

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideBrowseProjectsViewModelFactory(getProjectsUseCase: GetProjectsUseCase): ResourceListViewModelFactory<
                Project,
                UiProjectPreviewItem,
                GetProjectsUseCase,
                Params> {
            return ResourceListViewModelFactory(getProjectsUseCase, Project::toUiProjectPreviewItem)
        }
    }

    @Binds
    abstract fun bindContext(application: Application): Context

    @ContributesAndroidInjector
    abstract fun contributesRepoListActivity(): BrowseProjectsActivity

    @ContributesAndroidInjector
    abstract fun contributesSearchActivity(): SearchActivity

    @ContributesAndroidInjector
    abstract fun contributesProjectDetailActivity(): ProjectDetailActivity

    @ContributesAndroidInjector
    abstract fun contributesBrowseFragment(): BrowseFragment


}