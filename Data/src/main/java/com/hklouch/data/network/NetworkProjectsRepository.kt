package com.hklouch.data.network

import com.hklouch.data.model.toProjectList
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
            } ?: throw it.error()!!
        }
    }


    override fun getProjects(next: Int?): Observable<ProjectList> {
        return githubReposService.getProjects(next).map {
            it.response()?.let {
                val pageLinks = PageLinks(it.headers())
                val nextIndex = pageLinks.nextPage((BROWSE_PAGING_PARAM))
                val lastIndex = pageLinks.lastPage((BROWSE_PAGING_PARAM))
                it.body()?.toProjectList(nextIndex, lastIndex)
            } ?: throw it.error()!!
        }
    }

}