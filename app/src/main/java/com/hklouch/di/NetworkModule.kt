package com.hklouch.di

import com.hklouch.data.network.GithubReposService
import com.hklouch.data.network.GithubReposServiceFactory
import com.hklouch.data.network.NetworkProjectsRepository
import com.hklouch.domain.repository.ProjectsRepository
import com.hklouch.githubrepos4cs.BuildConfig
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class RemoteModule {

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideGithubService(): GithubReposService {
            return GithubReposServiceFactory.makeGithubReposService(BuildConfig.DEBUG)
        }
    }

    @Binds
    abstract fun bindNetworkProjectsRepository(projectsRepository: NetworkProjectsRepository): ProjectsRepository
}