package com.hklouch.data.network

import com.hklouch.data.model.toProjectList
import com.hklouch.domain.model.ProjectList
import com.hklouch.domain.repository.ProjectsRepository
import io.reactivex.Observable
import javax.inject.Inject


class NetworkProjectsRepository @Inject constructor(private val githubReposService: GithubReposService) : ProjectsRepository {

    override fun searchProjects(query: String, page: Int?, resultsPerPage: Int?): Observable<ProjectList> {
        return githubReposService.search(query = query, page = page, resultsPerPage = resultsPerPage ?: 40).map {
            it.response()?.let {
                val pageLinks = PageLinks(it.headers())
                it.body()?.toProjectList(pageLinks.nextIndex, pageLinks.lastIndex)
            } ?: throw it.error()!!
        }
    }


    override fun getProjects(next: Int?): Observable<ProjectList> {
        return githubReposService.getProjects(next).map {
            it.response()?.let {
                val pageLinks = PageLinks(it.headers())
                it.body()?.toProjectList(pageLinks.nextIndex, pageLinks.lastIndex)
            } ?: throw it.error()!!
        }
    }

}