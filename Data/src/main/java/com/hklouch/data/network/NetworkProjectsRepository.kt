package com.hklouch.data.network

import com.hklouch.data.model.toProject
import com.hklouch.data.model.toProjectList
import com.hklouch.domain.model.Project
import com.hklouch.domain.model.ProjectList
import com.hklouch.domain.repository.ProjectsRepository
import io.reactivex.Observable
import javax.inject.Inject


class NetworkProjectsRepository @Inject constructor(private val githubReposService: GithubReposService) : ProjectsRepository {

    companion object {
        private const val SEARCH_PAGING_PARAM = "page"
        private const val BROWSE_PAGING_PARAM = "since"
        private const val DEFAULT_RESULTS_PER_PAGE = 50
    }

    override fun searchProjects(query: String, page: Int?, resultsPerPage: Int?): Observable<ProjectList> {
        return githubReposService.search(query = query, page = page, resultsPerPage = resultsPerPage
                ?: DEFAULT_RESULTS_PER_PAGE).map {
            it.response()?.let {
                val pageLinks = PageLinks(it.headers())
                val nextIndex = pageLinks.nextPage((SEARCH_PAGING_PARAM))
                val lastIndex = pageLinks.lastPage((SEARCH_PAGING_PARAM))
                it.body()?.toProjectList(nextIndex, lastIndex)
            } ?: it.error()?.let { throw it } ?: throw Exception("Unknown problem occurred")
        }
    }


    override fun getProjects(next: Int?): Observable<ProjectList> {
        return githubReposService.getProjects(next).map {
            it.response()?.let {
                val pageLinks = PageLinks(it.headers())
                val nextIndex = pageLinks.nextPage((BROWSE_PAGING_PARAM))
                val lastIndex = pageLinks.lastPage((BROWSE_PAGING_PARAM))
                it.body()?.toProjectList(nextIndex, lastIndex)
            } ?: it.error()?.let { throw it } ?: throw Exception("Unknown problem occurred")
        }
    }

    override fun getProject(ownerName: String, projectName: String): Observable<Project> {
        return githubReposService.getProject(ownerName, projectName).map { it.toProject() }
    }

}