package com.hklouch.presentation.browse

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.hklouch.domain.interactor.browse.GetProjectsUseCase
import com.hklouch.domain.interactor.browse.GetProjectsUseCase.Params
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
import io.reactivex.observers.DisposableObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Captor
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class BrowseViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var getProjectsUseCase = mock<GetProjectsUseCase>()

    private lateinit var browseViewModel: BrowseViewModel

    private val projectData = Data()

    @Captor
    private val captor = argumentCaptor<DisposableObserver<PagingWrapper<Project>>>()

    @Before
    fun before() {
        browseViewModel = BrowseViewModel(getProjectsUseCase, Project::toUiProjectPreviewItem, null)
    }

    @Test
    fun should_instantiation_trigger_loading_state() {
        //When
        browseViewModel = BrowseViewModel(getProjectsUseCase, Project::toUiProjectPreviewItem, null)
        //Then
        Truth.assertThat(browseViewModel.getResourceResult().value).isInstanceOf(Loading::class.java)
    }

    @Test
    fun should_fetch_projects_on_instantiation() {
        //Then
        verify(getProjectsUseCase, times(1)).execute(any(), eq(null))
    }

    @Test
    fun should_fetch_projects_execute_use_case() {
        //When
        browseViewModel.fetchResource(Params(2))
        //Then
        verify(getProjectsUseCase, times(1)).execute(any(), eq(Params(2)))
    }

    @Test
    fun should_fetch_projects_return_success() {
        //Given
        val projectList = projectData.domain.projectList
        browseViewModel.fetchResource(Params(2))
        verify(getProjectsUseCase, times(1)).execute(captor.capture(), eq(Params(2)))
        //When
        captor.firstValue.onNext(projectList)
        //Then
        Truth.assertThat(browseViewModel.getResourceResult().value).isInstanceOf(Success::class.java)
    }

    @Test
    fun should_fetch_projects_return_data() {
        //Given
        val projectList = projectData.domain.projectList
        val expected = projectData.ui.uiPagingModel
        browseViewModel.fetchResource(Params(2))
        verify(getProjectsUseCase, times(1)).execute(captor.capture(), eq(Params(2)))
        //When
        captor.firstValue.onNext(projectList)
        //Then
        Truth.assertThat((browseViewModel.getResourceResult().value as Success).data).isEqualTo(expected)
    }

    @Test
    fun should_fetch_projects_return_error() {
        //Given
        browseViewModel.fetchResource(Params(2))
        verify(getProjectsUseCase, times(1)).execute(captor.capture(), eq(Params(2)))
        //When
        captor.firstValue.onError(Exception())
        //Then
        Truth.assertThat(browseViewModel.getResourceResult().value).isInstanceOf(Error::class.java)
    }

    @Test
    fun should_fetch_projects_return_error_throwable() {
        //Given
        val expected = Exception("Nasty exception !")
        browseViewModel.fetchResource(Params(2))
        verify(getProjectsUseCase, times(1)).execute(captor.capture(), eq(Params(2)))
        //When
        captor.firstValue.onError(expected)
        //Then
        Truth.assertThat((browseViewModel.getResourceResult().value as Error).throwable).isEqualTo(expected)
    }
}