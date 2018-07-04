package com.hklouch.githubrepos4cs.ui.browse

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.hklouch.domain.interactor.browse.GetProjectsUseCase
import com.hklouch.domain.interactor.browse.GetProjectsUseCase.Params
import com.hklouch.domain.model.ProjectList
import com.hklouch.githubrepos4cs.ui.Data
import com.hklouch.ui.State.Error
import com.hklouch.ui.State.Loading
import com.hklouch.ui.State.Success
import com.hklouch.ui.browse.ProjectsObserver
import com.hklouch.ui.browse.ReposListViewModel
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

class ReposListViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var getProjectsUseCase = mock<GetProjectsUseCase>()

    private lateinit var reposListViewModel: ReposListViewModel

    private val projectData = Data()

    @Captor
    private val captor = argumentCaptor<DisposableObserver<ProjectList>>()

    @Before
    fun before() {
        reposListViewModel = ReposListViewModel(getProjectsUseCase)
    }

    @Test
    fun should_instantiation_trigger_loading_state() {
        //When
        reposListViewModel = ReposListViewModel(getProjectsUseCase)
        //Then
        Truth.assertThat(reposListViewModel.getResultRepos().value).isInstanceOf(Loading::class.java)
    }

    @Test
    fun should_instantiation_fetch_projects() {
        //Then
        verify(getProjectsUseCase, times(1)).execute(any<ProjectsObserver>(), eq(Params(null)))
    }

    @Test
    fun should_fetch_projects_execute_use_case() {
        //When
        reposListViewModel.fetchRepos(2)
        //Then
        verify(getProjectsUseCase, times(1)).execute(any<ProjectsObserver>(), eq(Params(2)))
    }

    @Test
    fun should_fetch_projects_return_success() {
        //Given
        val projectList = projectData.domain.projectList
        reposListViewModel.fetchRepos(2)
        verify(getProjectsUseCase, times(1)).execute(captor.capture(), eq(Params(2)))
        //When
        captor.firstValue.onNext(projectList)
        //Then
        Truth.assertThat(reposListViewModel.getResultRepos().value).isInstanceOf(Success::class.java)
    }

    @Test
    fun should_fetch_projects_return_data() {
        //Given
        val projectList = projectData.domain.projectList
        val expected = projectData.ui.uiPagingModel
        reposListViewModel.fetchRepos(2)
        verify(getProjectsUseCase, times(1)).execute(captor.capture(), eq(Params(2)))
        //When
        captor.firstValue.onNext(projectList)
        //Then
        Truth.assertThat((reposListViewModel.getResultRepos().value as Success).data).isEqualTo(expected)
    }

    @Test
    fun should_fetch_projects_return_error() {
        //Given
        reposListViewModel.fetchRepos(2)
        verify(getProjectsUseCase, times(1)).execute(captor.capture(), eq(Params(2)))
        //When
        captor.firstValue.onError(Exception())
        //Then
        Truth.assertThat(reposListViewModel.getResultRepos().value).isInstanceOf(Error::class.java)
    }

    @Test
    fun should_fetch_projects_return_error_throwable() {
        //Given
        val expected = Exception("Nasty exception !")
        reposListViewModel.fetchRepos(2)
        verify(getProjectsUseCase, times(1)).execute(captor.capture(), eq(Params(2)))
        //When
        captor.firstValue.onError(expected)
        //Then
        Truth.assertThat((reposListViewModel.getResultRepos().value as Error).throwable).isEqualTo(expected)
    }
}