package com.hklouch.data.network

import com.hklouch.data.model.toProject
import com.hklouch.domain.model.Project
import com.hklouch.domain.repository.ProjectsRepository
import io.reactivex.Observable
import javax.inject.Inject


class NetworkProjectsRepository @Inject constructor(private val githubReposService: GithubReposService) : ProjectsRepository {

    override fun searchProjects(query: String): Observable<List<Project>> {
        return githubReposService.search(query).map { it.projects.map { it.toProject() } }
    }


    override fun getProjects(next: String?): Observable<List<Project>> {
        return githubReposService.getProjects(next).map { it.map { it.toProject() } }
//            it.error()?.let { throw it } ?: it.response()!!.body()!!.toProjectList()
    }
}
