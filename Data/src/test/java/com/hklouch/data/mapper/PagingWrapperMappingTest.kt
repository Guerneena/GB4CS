package com.hklouch.data.mapper

import com.google.common.truth.Truth
import com.hklouch.data.network.project.ProjectJson
import com.hklouch.data.network.project.toProjectList
import com.hklouch.data.network.toPagingWrapper
import com.hklouch.domain.model.PagingWrapper
import okhttp3.Headers
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result

class PagingWrapperMappingTest {

    private lateinit var projectData: Data

    @Before
    fun before() {
        projectData = Data()
    }

    @Test
    fun should_extract_next_pagination_param_from_headers() {
        //Given
        val responseHeaders = Headers.of(mapOf("Link" to "<https://api.github.com/search/repositories?q=tetris&page=3>; rel=\"next\""))
        val projectList = projectData.json.projectList
        val response = Response.success(projectList, responseHeaders)
        val expected = PagingWrapper(nextPage = 3,
                                     lastPage = null,
                                     items = projectData.domain.projectList)
        val result = Result.response(response)
        val mapper = { projectList.toProjectList() }
        //When
        val pagingWrapper = result.toPagingWrapper("page", mapper)

        //Then
        Truth.assertThat(pagingWrapper).isEqualTo(expected)
    }

    @Test
    fun should_extract_last_pagination_param_from_headers() {
        //Given
        val projectList = projectData.json.projectList
        val responseHeaders = Headers.of(mapOf("Link" to "<https://api.github.com/search/repositories?q=tetris&page=38>; rel=\"last\""))
        val response = Response.success(projectList, responseHeaders)
        val result = Result.response(response)
        val expected = PagingWrapper(nextPage = null,
                                     lastPage = 38,
                                     items = projectData.domain.projectList)
        val mapper = { projectList.toProjectList() }
        //When
        val pagingWrapper = result.toPagingWrapper("page", mapper)
        //Then
        Truth.assertThat(pagingWrapper).isEqualTo(expected)
    }

    @Test
    fun should__extract_next_and_last_pagination_param_from_headers() {
        //Given
        val projectList = projectData.json.projectList
        val responseHeaders = Headers.of(mapOf("Link" to "<https://api.github.com/search/repositories?q=tetris&page=2>; rel=\"next\",<https://api.github.com/search/repositories?q=tetris&page=38>; rel=\"last\""))
        val response = Response.success(projectList, responseHeaders)
        val result = Result.response(response)
        val expected = PagingWrapper(nextPage = 2,
                                     lastPage = 38,
                                     items = projectData.domain.projectList)
        val mapper = { projectList.toProjectList() }
        //When
        val pagingWrapper = result.toPagingWrapper("page", mapper)
        //Then
        Truth.assertThat(pagingWrapper).isEqualTo(expected)
    }

    @Test
    fun should_pagination_be_null_when_no_url_params() {
        //Given
        val projectList = projectData.json.projectList
        val responseHeaders = Headers.of(mapOf("Link" to "<https://api.github.com/search/repositories?q=tetris>; rel=\"next\",<https://api.github.com/search/repositories?q=tetris>; rel=\"last\""))
        val response = Response.success(projectList, responseHeaders)
        val result = Result.response(response)
        val expected = PagingWrapper(nextPage = null,
                                     lastPage = null,
                                     items = projectData.domain.projectList)
        val mapper = { projectList.toProjectList() }
        //When
        val pagingWrapper = result.toPagingWrapper("page", mapper)
        //Then
        Truth.assertThat(pagingWrapper).isEqualTo(expected)
    }

    @Test
    fun should_pagination_be_null_when_no_header_key() {
        //Given
        val projectList = projectData.json.projectList
        val responseHeaders = Headers.of(mapOf("other_header" to "other_value"))
        val response = Response.success(projectList, responseHeaders)
        val result = Result.response(response)
        val expected = PagingWrapper(nextPage = null,
                                     lastPage = null,
                                     items = projectData.domain.projectList)
        val mapper = { projectList.toProjectList() }
        //When
        val pagingWrapper = result.toPagingWrapper("page", mapper)
        //Then
        Truth.assertThat(pagingWrapper).isEqualTo(expected)
    }

    @Test
    fun should_pagination_be_null_when_no_headers() {
        //Given
        val projectList = projectData.json.projectList
        val responseHeaders = Headers.of(mapOf())
        val response = Response.success(projectList, responseHeaders)
        val result = Result.response(response)
        val expected = PagingWrapper(nextPage = null,
                                     lastPage = null,
                                     items = projectData.domain.projectList)
        val mapper = { projectList.toProjectList() }
        //When
        val pagingWrapper = result.toPagingWrapper("page", mapper)
        //Then
        Truth.assertThat(pagingWrapper).isEqualTo(expected)
    }

    @Test(expected = IllegalArgumentException::class)
    fun should_raise_exception_if_response_error() {
        //Given
        val errorBody = ResponseBody.create(null, "Broken!")
        val response = Response.error<List<ProjectJson>>(400, errorBody)
        val result = Result.response(response)
        val mapper = { projectData.json.projectList.toProjectList() }
        //When
        result.toPagingWrapper("page", mapper)
    }

    @Test(expected = Exception::class)
    fun should_raise_exception_if_result_error() {
        //Given
        val expectedException = Exception("I'm broken!")
        val result = Result.error<List<ProjectJson>>(expectedException)
        val mapper = { projectData.json.projectList.toProjectList() }
        //When
        result.toPagingWrapper("page", mapper)
    }

    @Test(expected = IllegalArgumentException::class)
    fun should_raise_exception_if_mapper_is_null() {
        //Given
        val errorBody = ResponseBody.create(null, "Broken!")
        val response = Response.error<List<ProjectJson>>(400, errorBody)
        val result = Result.response(response)
        val list: List<ProjectJson>? = null
        val mapper = { list?.toProjectList() }
        //When
        result.toPagingWrapper("page", mapper)
    }
}