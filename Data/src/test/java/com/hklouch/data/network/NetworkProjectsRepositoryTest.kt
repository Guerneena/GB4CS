package com.hklouch.data.network

import com.hklouch.data.mapper.Data
import com.hklouch.data.network.project.ProjectJson
import com.hklouch.data.network.project.ProjectSearchResponse
import com.hklouch.domain.interactor.pull.PullsUseCase.Params.State.OPEN
import com.hklouch.domain.model.PagingWrapper
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.Headers
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result

class NetworkProjectsRepositoryTest {

    private lateinit var projectData: Data

    @Mock private lateinit var githubReposService: GithubReposService

    private lateinit var projectsRepository: NetworkProjectsRepository

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        projectData = Data()
        projectsRepository = NetworkProjectsRepository(githubReposService)
    }

    @Test
    fun should_get_projects_return_data() {
        //Given
        val response = Response.success(listOf(projectData.json.project))
        val getProjectsResult = Result.response(response)
        given(githubReposService.getProjects(anyInt())).willReturn(Observable.just(getProjectsResult))
        val expected = PagingWrapper(nextPage = null,
                                     lastPage = null,
                                     items = listOf(projectData.domain.project))
        //When
        projectsRepository.getProjects(1).test()
                //Then
                .assertValue(expected)
    }

    @Test
    fun should_get_projects_complete() {
        //Given
        val response = Response.success(listOf(projectData.json.project))
        val getProjectsResult = Result.response(response)
        given(githubReposService.getProjects(anyInt())).willReturn(Observable.just(getProjectsResult))
        //When
        projectsRepository.getProjects(1).test()
                //Then
                .assertComplete()
    }

    @Test
    fun should_get_projects_extract_next_pagination_param_from_headers() {
        //Given
        val responseHeaders = Headers.of(mapOf("Link" to "<https://api.github.com/repositories?since=364>; rel=\"next\""))
        val response = Response.success(listOf(projectData.json.project), responseHeaders)
        val getProjectsResult = Result.response(response)
        given(githubReposService.getProjects(anyInt())).willReturn(Observable.just(getProjectsResult))
        val expected = PagingWrapper(nextPage = 364,
                                     lastPage = null,
                                     items = listOf(projectData.domain.project))
        //When
        projectsRepository.getProjects(1).test()
                //Then
                .assertValue(expected)
    }

    @Test
    fun should_get_projects_extract_last_pagination_param_from_headers() {
        //Given
        val responseHeaders = Headers.of(mapOf("Link" to "<https://api.github.com/repositories?since=833>; rel=\"last\""))
        val response = Response.success(listOf(projectData.json.project), responseHeaders)
        val getProjectsResult = Result.response(response)
        given(githubReposService.getProjects(anyInt())).willReturn(Observable.just(getProjectsResult))
        val expected = PagingWrapper(nextPage = null,
                                     lastPage = 833,
                                     items = listOf(projectData.domain.project))
        //When
        projectsRepository.getProjects(1).test()
                //Then
                .assertValue(expected)
    }

    @Test
    fun should_get_projects_extract_next_and_last_pagination_param_from_headers() {
        //Given
        val responseHeaders = Headers.of(mapOf("Link" to "<https://api.github.com/repositories?since=362>; rel=\"next\",<https://api.github.com/repositories?since=2010784>; rel=\"last\""))
        val response = Response.success(listOf(projectData.json.project), responseHeaders)
        val getProjectsResult = Result.response(response)
        given(githubReposService.getProjects(anyInt())).willReturn(Observable.just(getProjectsResult))
        val expected = PagingWrapper(nextPage = 362,
                                     lastPage = 2010784,
                                     items = listOf(projectData.domain.project))
        //When
        projectsRepository.getProjects(1).test()
                //Then
                .assertValue(expected)
    }

    @Test
    fun should_get_projects_emit_unknown_error() {
        //Given
        val errorBody = ResponseBody.create(null, "Broken!")
        val response = Response.error<List<ProjectJson>>(400, errorBody)
        val getProjectsResult = Result.response(response)
        given(githubReposService.getProjects(anyInt())).willReturn(Observable.just(getProjectsResult))
        //When
        projectsRepository.getProjects(1).test()
                //Then
                .assertError { it.message == "Unknown problem occurred" }
    }

    @Test
    fun should_get_projects_emit_error() {
        //Given
        val expectedException = Exception("I'm broken!")
        val getProjectsResult = Result.error<List<ProjectJson>>(expectedException)
        given(githubReposService.getProjects(anyInt())).willReturn(Observable.just(getProjectsResult))
        //When
        projectsRepository.getProjects(1).test()
                //Then
                .assertError { it == expectedException }
    }

    @Test
    fun should_search_projects_return_data() {
        //Given
        val response = Response.success(projectData.json.projectSearchResponse)
        val searchProjectsResult = Result.response(response)
        given(githubReposService.search("q", 1, 20)).willReturn(Observable.just(searchProjectsResult))
        val expected = PagingWrapper(nextPage = null,
                                     lastPage = null,
                                     items = projectData.domain.projectList)
        //When
        projectsRepository.searchProjects(query = "q",
                                          page = 1,
                                          resultsPerPage = 20).test()
                //Then
                .assertValue(expected)
    }

    @Test
    fun should_search_projects_complete() {
        //Given
        val response = Response.success(projectData.json.projectSearchResponse)
        val searchProjectsResult = Result.response(response)
        given(githubReposService.search("q", 1, 20)).willReturn(Observable.just(searchProjectsResult))
        //When
        projectsRepository.searchProjects(query = "q",
                                          page = 1,
                                          resultsPerPage = 20).test()
                //Then
                .assertComplete()
    }

    @Test
    fun should_search_projects_extract_next_pagination_param_from_headers() {
        //Given
        val responseHeaders = Headers.of(mapOf("Link" to "<https://api.github.com/search/repositories?q=tetris&page=3>; rel=\"next\""))
        val response = Response.success(projectData.json.projectSearchResponse, responseHeaders)
        val searchProjectsResult = Result.response(response)
        given(githubReposService.search("q", 1, 20)).willReturn(Observable.just(searchProjectsResult))
        val expected = PagingWrapper(nextPage = 3,
                                     lastPage = null,
                                     items = projectData.domain.projectList)
        //When
        projectsRepository.searchProjects(query = "q",
                                          page = 1,
                                          resultsPerPage = 20).test()
                //Then
                .assertValue(expected)
    }

    @Test
    fun should_search_projects_extract_last_pagination_param_from_headers() {
        //Given
        val responseHeaders = Headers.of(mapOf("Link" to "<https://api.github.com/search/repositories?q=tetris&page=38>; rel=\"last\""))
        val response = Response.success(projectData.json.projectSearchResponse, responseHeaders)
        val searchProjectsResult = Result.response(response)
        given(githubReposService.search("q", 1, 20)).willReturn(Observable.just(searchProjectsResult))
        val expected = PagingWrapper(nextPage = null,
                                     lastPage = 38,
                                     items = projectData.domain.projectList)
        //When
        projectsRepository.searchProjects(query = "q",
                                          page = 1,
                                          resultsPerPage = 20).test()
                //Then
                .assertValue(expected)
    }

    @Test
    fun should_search_projects_extract_next_and_last_pagination_param_from_headers() {
        //Given
        val responseHeaders = Headers.of(mapOf("Link" to "<https://api.github.com/search/repositories?q=tetris&page=2>; rel=\"next\",<https://api.github.com/search/repositories?q=tetris&page=38>; rel=\"last\""))
        val response = Response.success(projectData.json.projectSearchResponse, responseHeaders)
        val searchProjectsResult = Result.response(response)
        given(githubReposService.search("q", 1, 20)).willReturn(Observable.just(searchProjectsResult))
        val expected = PagingWrapper(nextPage = 2,
                                     lastPage = 38,
                                     items = projectData.domain.projectList)
        //When
        projectsRepository.searchProjects(query = "q",
                                          page = 1,
                                          resultsPerPage = 20).test()
                //Then
                .assertValue(expected)
    }

    @Test
    fun should_search_projects_emit_unknown_error() {
        //Given
        val errorBody = ResponseBody.create(null, "Broken!")
        val response = Response.error<ProjectSearchResponse>(400, errorBody)
        val searchProjectsResult = Result.response(response)
        given(githubReposService.search("q", 1, 20)).willReturn(Observable.just(searchProjectsResult))
        //When
        projectsRepository.searchProjects(query = "q",
                                          page = 1,
                                          resultsPerPage = 20).test()
                //Then
                .assertError { it.message == "Unknown problem occurred" }
    }

    @Test
    fun should_search_projects_emit_error() {
        //Given
        val expectedException = Exception("I'm broken!")
        val searchProjectsResult = Result.error<ProjectSearchResponse>(expectedException)
        given(githubReposService.search("q", 1, 2)).willReturn(Observable.just(searchProjectsResult))
        //When
        projectsRepository.searchProjects(query = "q",
                                          page = 1,
                                          resultsPerPage = 2).test()
                //Then
                .assertError { it == expectedException }
    }

    @Test
    fun should_get_project_details_return_data() {
        //Given
        val getProjectResult = projectData.json.project
        given(githubReposService.getProject(anyString(), anyString())).willReturn(Single.just(getProjectResult))
        val expected = projectData.domain.project
        //When
        projectsRepository.getProject("foo", "bar").test()
                //Then
                .assertValue(expected)
    }

    @Test
    fun should_get_project_details_complete() {
        //Given
        val getProjectResult = projectData.json.project
        given(githubReposService.getProject(anyString(), anyString())).willReturn(Single.just(getProjectResult))
        //When
        projectsRepository.getProject("foo", "bar").test()
                //Then
                .assertComplete()
    }

    @Test
    fun should_get_branches_return_data() {
        //Given
        val response = Response.success(projectData.json.branchList)
        val result = Result.response(response)
        given(githubReposService.getBranches("owner", "project", page = 1, resultsPerPage = 50)).willReturn(Observable.just(result))
        val expected = PagingWrapper(nextPage = null,
                                     lastPage = null,
                                     items = projectData.domain.branchList)
        //When
        projectsRepository.getBranches("owner", "project", page = 1, resultsPerPage = 50).test()
                //Then
                .assertValue(expected)
    }

    @Test
    fun should_get_branches_complete() {
        //Given
        val response = Response.success(projectData.json.branchList)
        val result = Result.response(response)
        given(githubReposService.getBranches("owner", "project", page = 1, resultsPerPage = 50)).willReturn(Observable.just(result))
        //When
        projectsRepository.getBranches("owner", "project", page = 1, resultsPerPage = 50).test()
                //Then
                .assertComplete()
    }

    @Test
    fun should_get_pulls_return_data() {
        //Given
        val response = Response.success(projectData.json.pullList)
        val result = Result.response(response)
        given(githubReposService.getPulls("owner", "project", page = 1, resultsPerPage = 50, state = "open"))
                .willReturn(Observable.just(result))
        val expected = PagingWrapper(nextPage = null,
                                     lastPage = null,
                                     items = projectData.domain.pullList)
        //When
        projectsRepository.getPulls("owner", "project", page = 1, resultsPerPage = 50, state = OPEN).test()
                //Then
                .assertValue(expected)
    }

    @Test
    fun should_get_pulls_complete() {
        //Given
        val response = Response.success(projectData.json.pullList)
        val result = Result.response(response)
        given(githubReposService.getPulls("owner", "project", page = 1, resultsPerPage = 50, state = "open"))
                .willReturn(Observable.just(result))
        //When
        projectsRepository.getPulls("owner", "project", page = 1, resultsPerPage = 50, state = OPEN).test()
                //Then
                .assertComplete()
    }

    @Test
    fun should_get_issues_return_data() {
        //Given
        val response = Response.success(projectData.json.issueList)
        val searchProjectsResult = Result.response(response)
        given(githubReposService.getIssues("owner", "project", page = 1, resultsPerPage = 50))
                .willReturn(Observable.just(searchProjectsResult))
        val expected = PagingWrapper(nextPage = null,
                                     lastPage = null,
                                     items = projectData.domain.issueList)
        //When
        projectsRepository.getIssues("owner", "project", page = 1, resultsPerPage = 50).test()
                //Then
                .assertValue(expected)
    }

    @Test
    fun should_get_issues_complete() {
        //Given
        val response = Response.success(projectData.json.issueList)
        val result = Result.response(response)
        given(githubReposService.getIssues("owner", "project", page = 1, resultsPerPage = 50))
                .willReturn(Observable.just(result))
        //When
        projectsRepository.getIssues("owner", "project", page = 1, resultsPerPage = 50).test()
                //Then
                .assertComplete()
    }

    @Test
    fun should_get_contributor_return_data() {
        //Given
        val response = Response.success(projectData.json.contributorList)
        val result = Result.response(response)
        given(githubReposService.getContributors("owner", "project", page = 1, resultsPerPage = 50))
                .willReturn(Observable.just(result))
        val expected = PagingWrapper(nextPage = null,
                                     lastPage = null,
                                     items = projectData.domain.contributorList)
        //When
        projectsRepository.getContributors("owner", "project", page = 1, resultsPerPage = 50).test()
                //Then
                .assertValue(expected)
    }

    @Test
    fun should_get_contributor_complete() {
        //Given
        val response = Response.success(projectData.json.contributorList)
        val result = Result.response(response)
        given(githubReposService.getContributors("owner", "project", page = 1, resultsPerPage = 50))
                .willReturn(Observable.just(result))
        //When
        projectsRepository.getContributors("owner", "project", page = 1, resultsPerPage = 50).test()
                //Then
                .assertComplete()
    }


}