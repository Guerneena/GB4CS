package com.hklouch.data.network

import com.hklouch.data.network.branch.BranchJson
import com.hklouch.data.network.issue.IssueJson
import com.hklouch.data.network.project.ProjectJson
import com.hklouch.data.network.project.ProjectSearchResponse
import com.hklouch.data.network.pull.PullJson
import com.hklouch.data.network.user.UserJson
import com.hklouch.data.network.util.PageLinks
import com.hklouch.data.network.util.lastPage
import com.hklouch.data.network.util.nextPage
import com.hklouch.domain.model.PagingWrapper
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
               @Query("page") page: Int?,
               @Query("per_page") resultsPerPage: Int?
    ): Observable<Result<ProjectSearchResponse>>

    @GET("repos/{owner}/{project}")
    fun getProject(@Path("owner") ownerName: String,
                   @Path("project") projectName: String): Single<ProjectJson>


    @GET("repos/{owner}/{project}/branches")
    fun getBranches(@Path("owner") ownerName: String,
                    @Path("project") projectName: String,
                    @Query("page") page: Int?,
                    @Query("per_page") resultsPerPage: Int?): Observable<Result<List<BranchJson>>>

    @GET("repos/{owner}/{project}/pulls")
    fun getPulls(@Path("owner") ownerName: String,
                 @Path("project") projectName: String,
                 @Query("state") state: String?,
                 @Query("page") page: Int?,
                 @Query("per_page") resultsPerPage: Int?): Observable<Result<List<PullJson>>>


    @GET("repos/{owner}/{project}/contributors")
    fun getContributors(@Path("owner") ownerName: String,
                        @Path("project") projectName: String,
                        @Query("page") page: Int?,
                        @Query("per_page") resultsPerPage: Int?): Observable<Result<List<UserJson>>>


    @GET("repos/{owner}/{project}/issues")
    fun getIssues(@Path("owner") ownerName: String,
                  @Path("project") projectName: String,
                  @Query("page") page: Int?,
                  @Query("per_page") resultsPerPage: Int?): Observable<Result<List<IssueJson>>>


}

fun <T, R> getPagingWrapper(result: Result<T>,
                                    pagingParameter: String,
                                    mapper: () -> List<R>?): PagingWrapper<R> {
    val resultList = mapper()
    val response = result.response()
    if (resultList == null || response == null) {
        result.error()?.let { throw it } ?: throw Exception("Unknown problem occurred")
    }
    val pageLinks = PageLinks(response.headers())
    val nextIndex = pageLinks.nextPage(pagingParameter)
    val lastIndex = pageLinks.lastPage(pagingParameter)
    return PagingWrapper(nextIndex, lastIndex, resultList)

}