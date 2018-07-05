package com.hklouch.data.network

import com.hklouch.data.network.branch.toBranchList
import com.hklouch.data.network.issue.toIssues
import com.hklouch.data.network.project.toProject
import com.hklouch.data.network.project.toProjectList
import com.hklouch.data.network.pull.toPulls
import com.hklouch.data.network.user.toUsers
import com.hklouch.domain.model.Branch
import com.hklouch.domain.model.Issue
import com.hklouch.domain.model.PagingWrapper
import com.hklouch.domain.model.Project
import com.hklouch.domain.model.Pull
import com.hklouch.domain.model.User
import com.hklouch.domain.repository.ProjectsRepository
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject


class NetworkProjectsRepository @Inject constructor(private val githubReposService: GithubReposService) : ProjectsRepository {

    companion object {
        private const val DEFAULT_PAGING_PARAM = "page"
        private const val BROWSE_PAGING_PARAM = "since"
        private const val DEFAULT_RESULTS_PER_PAGE = 50
    }

    override fun searchProjects(query: String, page: Int?, resultsPerPage: Int?): Observable<PagingWrapper<Project>> {
        return githubReposService.search(query = query, page = page, resultsPerPage = resultsPerPage
                ?: DEFAULT_RESULTS_PER_PAGE).map { result ->

            result.toPagingWrapper(pagingParameter = DEFAULT_PAGING_PARAM,
                                   mapper = { result.response()?.body()?.toProjectList() })
        }
    }


    override fun getProjects(next: Int?): Observable<PagingWrapper<Project>> {
        return githubReposService.getProjects(next).map { result ->

            result.toPagingWrapper(pagingParameter = BROWSE_PAGING_PARAM,
                                   mapper = { result.response()?.body()?.toProjectList() })
        }
    }

    override fun getProject(ownerName: String, projectName: String): Single<Project> {
        return githubReposService.getProject(ownerName, projectName).map { it.toProject() }
    }

    override fun getBranches(ownerName: String, projectName: String, page: Int?, resultsPerPage: Int?): Observable<PagingWrapper<Branch>> {
        return githubReposService.getBranches(ownerName,
                                              projectName, page,
                                              resultsPerPage ?: DEFAULT_RESULTS_PER_PAGE).map { result ->
            result.toPagingWrapper(pagingParameter = DEFAULT_PAGING_PARAM,
                                   mapper = { result.response()?.body()?.toBranchList() })

        }
    }

    override fun getContributors(ownerName: String, projectName: String, page: Int?, resultsPerPage: Int?): Observable<PagingWrapper<User>> {
        return githubReposService.getContributors(ownerName,
                                                  projectName,
                                                  page,
                                                  resultsPerPage ?: DEFAULT_RESULTS_PER_PAGE).map { result ->
            result.toPagingWrapper(pagingParameter = DEFAULT_PAGING_PARAM,
                                   mapper = { result.response()?.body()?.toUsers() })

        }
    }

    override fun getIssues(ownerName: String, projectName: String, page: Int?, resultsPerPage: Int?): Observable<PagingWrapper<Issue>> {
        return githubReposService.getIssues(ownerName,
                                            projectName,
                                            page,
                                            resultsPerPage ?: DEFAULT_RESULTS_PER_PAGE).map { result ->
            result.toPagingWrapper(pagingParameter = DEFAULT_PAGING_PARAM,
                                   mapper = { result.response()?.body()?.toIssues() })

        }
    }

    override fun getPulls(ownerName: String, projectName: String, state: String?, page: Int?, resultsPerPage: Int?): Observable<PagingWrapper<Pull>> {
        return githubReposService.getPulls(ownerName,
                                           projectName,
                                           state,
                                           page,
                                           resultsPerPage ?: DEFAULT_RESULTS_PER_PAGE).map { result ->
            result.toPagingWrapper(pagingParameter = DEFAULT_PAGING_PARAM,
                                   mapper = { result.response()?.body()?.toPulls() })

        }
    }
}