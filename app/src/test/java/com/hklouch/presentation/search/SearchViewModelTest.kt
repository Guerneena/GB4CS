package com.hklouch.presentation.search

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.hklouch.domain.interactor.search.SearchProjectsUseCase
import com.hklouch.domain.interactor.search.SearchProjectsUseCase.Params
import com.hklouch.domain.model.PagingWrapper
import com.hklouch.domain.model.Project
import com.hklouch.presentation.Data
import com.hklouch.ui.State.Error
import com.hklouch.ui.State.Loading
import com.hklouch.ui.State.Success
import com.hklouch.ui.model.toUiProjectPreviewItem
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import io.reactivex.observers.DisposableObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Captor
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class SearchViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var searchProjectsUseCase = mock<SearchProjectsUseCase>()

    private lateinit var searchViewModel: SearchViewModel

    private val projectData = Data()

    private val projectList = projectData.domain.projectList

    @Captor
    private val captor = argumentCaptor<DisposableObserver<PagingWrapper<Project>>>()

    @Before
    fun before() {
        searchViewModel = SearchViewModel(searchProjectsUseCase, Project::toUiProjectPreviewItem, null)
    }


    @Test
    fun should_not_fetch_resource_on_instantiation() {
        //When
        val initParams = Params("first")
        searchViewModel = SearchViewModel(searchProjectsUseCase, Project::toUiProjectPreviewItem, initParams)
        //Then
        verify(searchProjectsUseCase, never()).execute(any(), eq(initParams))
    }

    @Test
    fun should_fetch_resource_trigger_loading_state() {
        //When
        searchViewModel.fetchResource(Params("q"))
        //Then
        Truth.assertThat(searchViewModel.getResourceResult().value).isInstanceOf(Loading::class.java)
    }

    @Test
    fun should_fetch_resource_execute_use_case() {
        //When
        searchViewModel.fetchResource(Params("q"))
        //Then
        verify(searchProjectsUseCase, times(1)).execute(any(), eq(Params("q")))
    }

    @Test
    fun should_fetch_resource_return_success() {
        //Given
        searchViewModel.fetchResource(Params("q"))
        verify(searchProjectsUseCase, times(1)).execute(captor.capture(), eq(Params("q")))
        //When
        captor.firstValue.onNext(projectList)
        //Then
        Truth.assertThat(searchViewModel.getResourceResult().value).isInstanceOf(Success::class.java)
    }

    @Test
    fun should_fetch_resource_return_data() {
        //Given
        val expected = projectData.ui.uiPagingModel
        searchViewModel.fetchResource(Params("q"))
        verify(searchProjectsUseCase, times(1)).execute(captor.capture(), eq(Params("q")))
        //When
        captor.firstValue.onNext(projectList)
        //Then
        Truth.assertThat((searchViewModel.getResourceResult().value as Success).data).isEqualTo(expected)
    }

    @Test
    fun should_fetch_resource_return_error() {
        //Given
        searchViewModel.fetchResource(Params("q"))
        verify(searchProjectsUseCase, times(1)).execute(captor.capture(), eq(Params("q")))
        //When
        captor.firstValue.onError(Exception())
        //Then
        Truth.assertThat(searchViewModel.getResourceResult().value).isInstanceOf(Error::class.java)
    }

    @Test
    fun should_fetch_resource_return_error_throwable() {
        //Given
        val expected = Exception("Nasty exception !")
        searchViewModel.fetchResource(Params("q"))
        verify(searchProjectsUseCase, times(1)).execute(captor.capture(), eq(Params("q")))
        //When
        captor.firstValue.onError(expected)
        //Then
        Truth.assertThat((searchViewModel.getResourceResult().value as Error).throwable).isEqualTo(expected)
    }


    @Test
    fun should_request_next_page_execute_use_case_with_previous_query() {
        //Given
        searchViewModel.fetchResource(Params("q"))
        //When
        searchViewModel.fetchResource(Params("q", 2))
        //Then
        verify(searchProjectsUseCase, times(1)).execute(any(), eq(Params("q", 2)))
    }

    @Test
    fun should_request_next_page_trigger_loading_state() {
        //Given
        searchViewModel.fetchResource(Params("q"))
        //When
        searchViewModel.fetchResource(Params("q", 2))
        //Then
        Truth.assertThat(searchViewModel.getResourceResult().value).isInstanceOf(Loading::class.java)
    }

    @Test
    fun should_request_next_page_success() {
        //Given
        searchViewModel.fetchResource(Params("q"))
        //When
        searchViewModel.fetchResource(Params("q", 2))
        verify(searchProjectsUseCase, times(1)).execute(captor.capture(), eq(Params("q", 2)))
        captor.firstValue.onNext(projectList)
        //Then
        Truth.assertThat(searchViewModel.getResourceResult().value).isInstanceOf(Success::class.java)
    }

    @Test
    fun should_request_next_page_return_data() {
        //Given
        val expected = projectData.ui.uiPagingModel
        searchViewModel.fetchResource(Params("q"))
        //When
        searchViewModel.fetchResource(Params("q", 2))
        verify(searchProjectsUseCase, times(1)).execute(captor.capture(), eq(Params("q", 2)))
        captor.firstValue.onNext(projectList)
        //Then
        Truth.assertThat((searchViewModel.getResourceResult().value as Success).data).isEqualTo(expected)
    }

}