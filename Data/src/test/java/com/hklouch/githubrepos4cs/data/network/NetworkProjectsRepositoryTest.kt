package com.hklouch.githubrepos4cs.data.network

import com.hklouch.data.network.project.ProjectJson
import com.hklouch.data.network.project.ProjectSearchResponse
import com.hklouch.data.network.GithubReposService
import com.hklouch.data.network.NetworkProjectsRepository
import com.hklouch.domain.model.ProjectList
import com.hklouch.githubrepos4cs.data.mapper.Data
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
        val expected = ProjectList(nextPage = null,
                                   lastPage = null,
                                   projects = listOf(projectData.domain.project))
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
        val expected = ProjectList(nextPage = 364,
                                   lastPage = null,
                                   projects = listOf(projectData.domain.project))
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
        val expected = ProjectList(nextPage = null,
                                   lastPage = 833,
                                   projects = listOf(projectData.domain.project))
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
        val expected = ProjectList(nextPage = 362,
                                   lastPage = 2010784,
                                   projects = listOf(projectData.domain.project))
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
        given(githubReposService.search(anyString(), anyInt(), anyInt())).willReturn(Observable.just(searchProjectsResult))
        val expected = ProjectList(nextPage = null,
                                   lastPage = null,
                                   projects = listOf(projectData.domain.project))
        //When
        projectsRepository.searchProjects(query = "q",
                                          page = null,
                                          resultsPerPage = null).test()
                //Then
                .assertValue(expected)
    }

    @Test
    fun should_search_projects_complete() {
        //Given
        val response = Response.success(projectData.json.projectSearchResponse)
        val searchProjectsResult = Result.response(response)
        given(githubReposService.search(anyString(), anyInt(), anyInt())).willReturn(Observable.just(searchProjectsResult))
        //When
        projectsRepository.searchProjects(query = "q",
                                          page = null,
                                          resultsPerPage = null).test()
                //Then
                .assertComplete()
    }

    @Test
    fun should_search_projects_extract_next_pagination_param_from_headers() {
        //Given
        val responseHeaders = Headers.of(mapOf("Link" to "<https://api.github.com/search/repositories?q=tetris&page=3>; rel=\"next\""))
        val response = Response.success(projectData.json.projectSearchResponse, responseHeaders)
        val searchProjectsResult = Result.response(response)
        given(githubReposService.search(anyString(), anyInt(), anyInt())).willReturn(Observable.just(searchProjectsResult))
        val expected = ProjectList(nextPage = 3,
                                   lastPage = null,
                                   projects = listOf(projectData.domain.project))
        //When
        projectsRepository.searchProjects(query = "q",
                                          page = null,
                                          resultsPerPage = null).test()
                //Then
                .assertValue(expected)
    }

    @Test
    fun should_search_projects_extract_last_pagination_param_from_headers() {
        //Given
        val responseHeaders = Headers.of(mapOf("Link" to "<https://api.github.com/search/repositories?q=tetris&page=38>; rel=\"last\""))
        val response = Response.success(projectData.json.projectSearchResponse, responseHeaders)
        val searchProjectsResult = Result.response(response)
        given(githubReposService.search(anyString(), anyInt(), anyInt())).willReturn(Observable.just(searchProjectsResult))
        val expected = ProjectList(nextPage = null,
                                   lastPage = 38,
                                   projects = listOf(projectData.domain.project))
        //When
        projectsRepository.searchProjects(query = "q",
                                          page = null,
                                          resultsPerPage = null).test()
                //Then
                .assertValue(expected)
    }

    @Test
    fun should_search_projects_extract_next_and_last_pagination_param_from_headers() {
        //Given
        val responseHeaders = Headers.of(mapOf("Link" to "<https://api.github.com/search/repositories?q=tetris&page=2>; rel=\"next\",<https://api.github.com/search/repositories?q=tetris&page=38>; rel=\"last\""))
        val response = Response.success(projectData.json.projectSearchResponse, responseHeaders)
        val searchProjectsResult = Result.response(response)
        given(githubReposService.search(anyString(), anyInt(), anyInt())).willReturn(Observable.just(searchProjectsResult))
        val expected = ProjectList(nextPage = 2,
                                   lastPage = 38,
                                   projects = listOf(projectData.domain.project))
        //When
        projectsRepository.searchProjects(query = "q",
                                          page = null,
                                          resultsPerPage = null).test()
                //Then
                .assertValue(expected)
    }

    @Test
    fun should_search_projects_emit_unknown_error() {
        //Given
        val errorBody = ResponseBody.create(null, "Broken!")
        val response = Response.error<ProjectSearchResponse>(400, errorBody)
        val searchProjectsResult = Result.response(response)
        given(githubReposService.search(anyString(), anyInt(), anyInt())).willReturn(Observable.just(searchProjectsResult))
        //When
        projectsRepository.searchProjects(query = "q",
                                          page = null,
                                          resultsPerPage = null).test()
                //Then
                .assertError { it.message == "Unknown problem occurred" }
    }

    @Test
    fun should_search_projects_emit_error() {
        //Given
        val expectedException = Exception("I'm broken!")
        val searchProjectsResult = Result.error<ProjectSearchResponse>(expectedException)
        given(githubReposService.search(anyString(), anyInt(), anyInt())).willReturn(Observable.just(searchProjectsResult))
        //When
        projectsRepository.searchProjects(query = "q",
                                          page = null,
                                          resultsPerPage = null).test()
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
}