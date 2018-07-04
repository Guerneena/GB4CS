package com.hklouch.data.network

import com.hklouch.data.model.ProjectJson
import com.hklouch.data.model.ProjectSearchResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubReposService {

    @GET("repositories")
    fun getProjects(@Query("since") since: Int?): Observable<Result<List<ProjectJson>>>

    @GET("search/repositories")
    fun search(@Query("q") query: String,
               @Query("per_page") resultsPerPage: Int?,
               @Query("page") page: Int?
    ): Observable<Result<ProjectSearchResponse>>

    @GET("repos/{owner}/{project}")
    fun getProject(@Path("owner") ownerName: String,
                   @Path("project") projectName: String): Single<ProjectJson>


}