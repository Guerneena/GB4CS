package com.hklouch.githubrepos4cs.ui.search

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.hklouch.domain.interactor.search.SearchProjectsUseCase
import com.hklouch.domain.interactor.search.SearchProjectsUseCase.Params
import com.hklouch.domain.model.ProjectList
import com.hklouch.githubrepos4cs.ui.Data
import com.hklouch.ui.State.Error
import com.hklouch.ui.State.Loading
import com.hklouch.ui.State.Success
import com.hklouch.ui.browse.ProjectsObserver
import com.hklouch.ui.search.SearchViewModel
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
    private val captor = argumentCaptor<DisposableObserver<ProjectList>>()

    @Before
    fun before() {
        searchViewModel = SearchViewModel(searchProjectsUseCase)
    }

    @Test
    fun should_submit_query_trigger_loading_state() {
        //When
        searchViewModel.submitQuery("q")
        //Then
        Truth.assertThat(searchViewModel.getResultRepos().value).isInstanceOf(Loading::class.java)
    }

    @Test
    fun should_submit_query_execute_use_case() {
        //When
        searchViewModel.submitQuery("q")
        //Then
        verify(searchProjectsUseCase, times(1)).execute(any<ProjectsObserver>(), eq(Params("q")))
    }

    @Test
    fun should_submit_query_filter_out_empty_values() {
        //When
        searchViewModel.submitQuery("")
        //Then
        verify(searchProjectsUseCase, never()).execute(any<ProjectsObserver>(), eq(Params("")))
    }

    @Test
    fun should_submit_query_filter_out_blank_values() {
        //When
        val blankQuery = "  "
        searchViewModel.submitQuery(blankQuery)
        //Then
        verify(searchProjectsUseCase, never()).execute(any<ProjectsObserver>(), eq(Params(blankQuery)))
    }

    @Test
    fun should_submit_query_return_success() {
        //Given
        searchViewModel.submitQuery("q")
        verify(searchProjectsUseCase, times(1)).execute(captor.capture(), eq(Params("q")))
        //When
        captor.firstValue.onNext(projectList)
        //Then
        Truth.assertThat(searchViewModel.getResultRepos().value).isInstanceOf(Success::class.java)
    }

    @Test
    fun should_submit_query_return_data() {
        //Given

        val expected = projectData.ui.uiPagingModel
        searchViewModel.submitQuery("q")
        verify(searchProjectsUseCase, times(1)).execute(captor.capture(), eq(Params("q")))
        //When
        captor.firstValue.onNext(projectList)
        //Then
        Truth.assertThat((searchViewModel.getResultRepos().value as Success).data).isEqualTo(expected)
    }

    @Test
    fun should_submit_query_return_error() {
        //Given
        searchViewModel.submitQuery("q")
        verify(searchProjectsUseCase, times(1)).execute(captor.capture(), eq(Params("q")))
        //When
        captor.firstValue.onError(Exception())
        //Then
        Truth.assertThat(searchViewModel.getResultRepos().value).isInstanceOf(Error::class.java)
    }

    @Test
    fun should_submit_query_return_error_throwable() {
        //Given
        val expected = Exception("Nasty exception !")
        searchViewModel.submitQuery("q")
        verify(searchProjectsUseCase, times(1)).execute(captor.capture(), eq(Params("q")))
        //When
        captor.firstValue.onError(expected)
        //Then
        Truth.assertThat((searchViewModel.getResultRepos().value as Error).throwable).isEqualTo(expected)
    }

    @Test
    fun should_request_next_page_do_nothing_when_no_query_before() {
        //When
        searchViewModel.requestNextPage(2)
        //Then
        verify(searchProjectsUseCase, never()).execute(captor.capture(), any())
    }

    @Test
    fun should_request_next_page_execute_use_case_with_previous_query() {
        //Given
        searchViewModel.submitQuery("q")
        //When
        searchViewModel.requestNextPage(2)
        //Then
        verify(searchProjectsUseCase, times(1)).execute(any(), eq(Params("q", 2)))
    }

    @Test
    fun should_request_next_page_trigger_loading_state() {
        //Given
        searchViewModel.submitQuery("q")
        //When
        searchViewModel.requestNextPage(2)
        //Then
        Truth.assertThat(searchViewModel.getResultRepos().value).isInstanceOf(Loading::class.java)
    }

    @Test
    fun should_request_next_page_success() {
        //Given
        searchViewModel.submitQuery("q")
        //When
        searchViewModel.requestNextPage(2)
        verify(searchProjectsUseCase, times(1)).execute(captor.capture(), eq(Params("q", 2)))
        captor.firstValue.onNext(projectList)
        //Then
        Truth.assertThat(searchViewModel.getResultRepos().value).isInstanceOf(Success::class.java)
    }

    @Test
    fun should_request_next_page_return_data() {
        //Given
        val expected = projectData.ui.uiPagingModel
        searchViewModel.submitQuery("q")
        //When
        searchViewModel.requestNextPage(2)
        verify(searchProjectsUseCase, times(1)).execute(captor.capture(), eq(Params("q", 2)))
        captor.firstValue.onNext(projectList)
        //Then
        Truth.assertThat((searchViewModel.getResultRepos().value as Success).data).isEqualTo(expected)
    }

    @Test
    fun should_retry_execute_previous_query() {
        //Given
        searchViewModel.submitQuery("q")
        //When
        searchViewModel.retry()
        //Then
        verify(searchProjectsUseCase, times(2)).execute(any(), eq(Params("q")))
    }

    @Test
    fun should_retry_do_nothing_when_no_query_before() {
        //When
        searchViewModel.retry()
        //Then
        verify(searchProjectsUseCase, never()).execute(any(), any())
    }
}