package com.hklouch.domain.repository

import com.hklouch.domain.model.Project
import com.hklouch.domain.model.ProjectList
import io.reactivex.Observable

interface ProjectsRepository {

    fun getProjects(next: Int?): Observable<ProjectList>

    fun searchProjects(query: String, page: Int?, resultsPerPage: Int?): Observable<ProjectList>

    fun getProject(ownerName: String, projectName: String): Observable<Project>
}