package com.hklouch.presentation.detail

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.hklouch.domain.interactor.detail.GetProjectDetailUseCase
import com.hklouch.domain.interactor.detail.GetProjectDetailUseCase.Params
import com.hklouch.domain.model.Project
import com.hklouch.presentation.Data
import com.hklouch.ui.State.Error
import com.hklouch.ui.State.Loading
import com.hklouch.ui.State.Success
import com.hklouch.presentation.detail.ProjectDetailViewModel.ProjectDetailSubscriber
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.observers.DisposableSingleObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Captor
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class ProjectDetailViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var getProjectDetailUseCase = mock<GetProjectDetailUseCase>()

    private lateinit var projectDetailViewModel: ProjectDetailViewModel

    private val projectData = Data()

    @Captor
    private val captor = argumentCaptor<DisposableSingleObserver<Project>>()

    @Before
    fun before() {
        projectDetailViewModel = ProjectDetailViewModel(getProjectDetailUseCase, "randomOwner", "randomProject")
    }

    @Test
    fun should_instantiation_trigger_loading_state() {
        //When
        projectDetailViewModel = ProjectDetailViewModel(getProjectDetailUseCase, "owner", "project")
        //Then
        Truth.assertThat(projectDetailViewModel.getResultProjectDetail().value).isInstanceOf(Loading::class.java)
    }

    @Test
    fun should_instantiation_fetch_project() {
        //When
        projectDetailViewModel = ProjectDetailViewModel(getProjectDetailUseCase, "owner", "project")
        //Then
        verify(getProjectDetailUseCase, times(1))
                .execute(any<ProjectDetailSubscriber>(), eq(Params("owner", "project")))
    }

    @Test
    fun should_fetch_project_detail_execute_use_case() {
        //When
        val ownerName = "owner"
        val projectName = "project"
        projectDetailViewModel.fetchProjectDetail(ownerName, projectName)
        //Then
        verify(getProjectDetailUseCase, times(1))
                .execute(any<ProjectDetailSubscriber>(), eq(Params(ownerName, projectName)))
    }

    @Test
    fun should_fetch_project_detail_return_success() {
        //Given
        val project = projectData.domain.project
        val ownerName = "owner"
        val projectName = "project"
        projectDetailViewModel.fetchProjectDetail(ownerName, projectName)
        verify(getProjectDetailUseCase, times(1))
                .execute(captor.capture(), eq(Params(ownerName, projectName)))
        //When
        captor.firstValue.onSuccess(project)
        //Then
        Truth.assertThat(projectDetailViewModel.getResultProjectDetail().value).isInstanceOf(Success::class.java)
    }

    @Test
    fun should_fetch_project_detail_return_data() {
        //Given
        val project = projectData.domain.project
        val expected = projectData.ui.uiProjectItem
        val ownerName = "owner"
        val projectName = "project"
        projectDetailViewModel.fetchProjectDetail(ownerName, projectName)
        verify(getProjectDetailUseCase, times(1))
                .execute(captor.capture(), eq(Params(ownerName, projectName)))
        //When
        captor.firstValue.onSuccess(project)
        //Then
        Truth.assertThat((projectDetailViewModel.getResultProjectDetail().value as Success).data).isEqualTo(expected)
    }

    @Test
    fun should_fetch_project_detail_return_error() {
        //Given
        val ownerName = "owner"
        val projectName = "project"
        projectDetailViewModel.fetchProjectDetail(ownerName, projectName)
        verify(getProjectDetailUseCase, times(1))
                .execute(captor.capture(), eq(Params(ownerName, projectName)))
        //When
        captor.firstValue.onError(Exception())
        //Then
        Truth.assertThat(projectDetailViewModel.getResultProjectDetail().value).isInstanceOf(Error::class.java)
    }

    @Test
    fun should_fetch_project_detail_return_error_throwable() {
        //Given
        val expected = Exception("Nasty exception !")
        val ownerName = "owner"
        val projectName = "project"
        projectDetailViewModel.fetchProjectDetail(ownerName, projectName)
        verify(getProjectDetailUseCase, times(1))
                .execute(captor.capture(), eq(Params(ownerName, projectName)))
        //When
        captor.firstValue.onError(expected)
        //Then
        Truth.assertThat((projectDetailViewModel.getResultProjectDetail().value as Error).throwable).isEqualTo(expected)
    }
}