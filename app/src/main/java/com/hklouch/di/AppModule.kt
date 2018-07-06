package com.hklouch.di

import android.app.Application
import android.content.Context
import com.hklouch.domain.interactor.branch.BranchesUseCase
import com.hklouch.domain.interactor.browse.GetProjectsUseCase
import com.hklouch.domain.interactor.contributor.ContributorsUseCase
import com.hklouch.domain.interactor.issue.IssuesUseCase
import com.hklouch.domain.interactor.pull.PullsUseCase
import com.hklouch.domain.interactor.search.SearchProjectsUseCase
import com.hklouch.domain.model.Branch
import com.hklouch.domain.model.Issue
import com.hklouch.domain.model.Project
import com.hklouch.domain.model.Pull
import com.hklouch.domain.model.User
import com.hklouch.presentation.branch.BranchViewModelFactory
import com.hklouch.presentation.browse.BrowseViewModelFactory
import com.hklouch.presentation.contributor.ContributorViewModelFactory
import com.hklouch.presentation.issue.IssueViewModelFactory
import com.hklouch.presentation.pull.PullViewModelFactory
import com.hklouch.presentation.search.SearchViewModelFactory
import com.hklouch.ui.branch.BranchActivity
import com.hklouch.ui.branch.BranchFragment
import com.hklouch.ui.browse.BrowseFragment
import com.hklouch.ui.browse.BrowseProjectsActivity
import com.hklouch.ui.contributor.ContributorActivity
import com.hklouch.ui.contributor.ContributorFragment
import com.hklouch.ui.detail.ProjectDetailActivity
import com.hklouch.ui.issue.IssueActivity
import com.hklouch.ui.issue.IssueFragment
import com.hklouch.ui.model.toUiBranchItem
import com.hklouch.ui.model.toUiIssueItem
import com.hklouch.ui.model.toUiProjectPreviewItem
import com.hklouch.ui.model.toUiPullItem
import com.hklouch.ui.model.toUiUserItem
import com.hklouch.ui.pull.PullActivity
import com.hklouch.ui.pull.PullFragment
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
        fun provideBrowseProjectsViewModelFactory(getProjectsUseCase: GetProjectsUseCase): BrowseViewModelFactory {
            return BrowseViewModelFactory(getProjectsUseCase, Project::toUiProjectPreviewItem)
        }

        @Provides
        @JvmStatic
        fun provideBranchViewModelFactory(branchesUseCase: BranchesUseCase): BranchViewModelFactory {
            return BranchViewModelFactory(branchesUseCase, Branch::toUiBranchItem)
        }


        @Provides
        @JvmStatic
        fun provideIssueViewModelFactory(issuesUseCase: IssuesUseCase): IssueViewModelFactory {
            return IssueViewModelFactory(issuesUseCase, Issue::toUiIssueItem)
        }


        @Provides
        @JvmStatic
        fun providePullViewModelFactory(pullsUseCase: PullsUseCase): PullViewModelFactory {
            return PullViewModelFactory(pullsUseCase, Pull::toUiPullItem)
        }

        @Provides
        @JvmStatic
        fun provideContributorsViewModelFactory(contributorsUseCase: ContributorsUseCase): ContributorViewModelFactory {
            return ContributorViewModelFactory(contributorsUseCase, User::toUiUserItem)
        }

        @Provides
        @JvmStatic
        fun provideSearchViewModelFactory(searchUseCase: SearchProjectsUseCase): SearchViewModelFactory {
            return SearchViewModelFactory(searchUseCase, Project::toUiProjectPreviewItem)
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
    abstract fun contributesBranchActivity(): BranchActivity

    @ContributesAndroidInjector
    abstract fun contributesContributorActivity(): ContributorActivity

    @ContributesAndroidInjector
    abstract fun contributesIssueActivity(): IssueActivity

    @ContributesAndroidInjector
    abstract fun contributesPullActivity(): PullActivity

    @ContributesAndroidInjector
    abstract fun contributesBrowseFragment(): BrowseFragment

    @ContributesAndroidInjector
    abstract fun contributesBranchFragment(): BranchFragment

    @ContributesAndroidInjector
    abstract fun contributesIssueFragment(): IssueFragment

    @ContributesAndroidInjector
    abstract fun contributesPullFragment(): PullFragment

    @ContributesAndroidInjector
    abstract fun contributesContributorFragment(): ContributorFragment


}