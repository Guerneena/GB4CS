package com.hklouch.data.network

import com.hklouch.data.model.ProjectJson
import com.hklouch.data.model.ProjectSearchResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubReposService {

    @GET("repositories")
    fun getProjects(@Query("since") since: String? = null): Observable<List<ProjectJson>>

    @GET("search/repositories")
    fun search(@Query("q") query: String): Observable<ProjectSearchResponse>


}