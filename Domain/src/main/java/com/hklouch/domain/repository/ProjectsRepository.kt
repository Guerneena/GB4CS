package com.hklouch.domain.repository

import com.hklouch.domain.model.Branch
import com.hklouch.domain.model.Issue
import com.hklouch.domain.model.PagingWrapper
import com.hklouch.domain.model.Project
import com.hklouch.domain.model.Pull
import com.hklouch.domain.model.User
import io.reactivex.Observable
import io.reactivex.Single

interface ProjectsRepository {

    fun getProjects(next: Int?): Observable<PagingWrapper<Project>>

    fun searchProjects(query: String, page: Int?, resultsPerPage: Int?): Observable<PagingWrapper<Project>>

    fun getProject(ownerName: String, projectName: String): Single<Project>

    fun getBranches(ownerName: String, projectName: String, page: Int?, resultsPerPage: Int?): Observable<PagingWrapper<Branch>>

    fun getContributors(ownerName: String, projectName: String, page: Int?, resultsPerPage: Int?): Observable<PagingWrapper<User>>

    fun getIssues(ownerName: String, projectName: String, page: Int?, resultsPerPage: Int?): Observable<PagingWrapper<Issue>>

    fun getPulls(ownerName: String, projectName: String, state: String?, page: Int?, resultsPerPage: Int?): Observable<PagingWrapper<Pull>>

}