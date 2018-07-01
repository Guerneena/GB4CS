package com.hklouch.domain.repository

import com.hklouch.domain.model.Project
import io.reactivex.Observable

interface ProjectsRepository {

    fun getProjects(next: String? = null): Observable<List<Project>>

    fun searchProjects(next: String): Observable<List<Project>>
}