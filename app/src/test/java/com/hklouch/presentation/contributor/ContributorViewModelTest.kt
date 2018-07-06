package com.hklouch.presentation.contributor

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.hklouch.domain.interactor.contributor.ContributorsUseCase
import com.hklouch.domain.interactor.contributor.ContributorsUseCase.Params
import com.hklouch.domain.model.PagingWrapper
import com.hklouch.domain.model.User
import com.hklouch.presentation.Data
import com.hklouch.ui.State.Error
import com.hklouch.ui.State.Loading
import com.hklouch.ui.State.Success
import com.hklouch.ui.model.toUiUserItem
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

class ContributorViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var useCase = mock<ContributorsUseCase>()

    private lateinit var viewModel: ContributorViewModel

    private val projectData = Data()

    @Captor
    private val captor = argumentCaptor<DisposableObserver<PagingWrapper<User>>>()

    @Before
    fun before() {
        viewModel = ContributorViewModel(useCase, User::toUiUserItem, null)
    }

    @Test
    fun should_instantiation_trigger_loading_state() {
        //When
        viewModel = ContributorViewModel(useCase, User::toUiUserItem, null)
        //Then
        Truth.assertThat(viewModel.getResourceResult().value).isInstanceOf(Loading::class.java)
    }

    @Test
    fun should_fetch_resource_on_instantiation() {
        //Then
        verify(useCase, times(1)).execute(any(), eq(null))
    }

    @Test
    fun should_fetch_resource_execute_use_case() {
        //When
        viewModel.fetchResource(Params("owner", "project"))
        //Then
        verify(useCase, times(1)).execute(any(), eq(Params("owner", "project")))
    }

    @Test
    fun should_fetch_resource_return_success() {
        //Given
        val contributorList = projectData.domain.contributorList
        viewModel.fetchResource(Params("owner", "project"))
        verify(useCase, times(1)).execute(captor.capture(), eq(Params("owner", "project")))
        //When
        captor.firstValue.onNext(contributorList)
        //Then
        Truth.assertThat(viewModel.getResourceResult().value).isInstanceOf(Success::class.java)
    }

    @Test
    fun should_fetch_resource_return_data() {
        //Given
        val contributorList = projectData.domain.contributorList
        val expected = projectData.ui.uiContributorList
        viewModel.fetchResource(Params("owner", "project"))
        verify(useCase, times(1)).execute(captor.capture(), eq(Params("owner", "project")))
        //When
        captor.firstValue.onNext(contributorList)
        //Then
        Truth.assertThat((viewModel.getResourceResult().value as Success).data).isEqualTo(expected)
    }

    @Test
    fun should_fetch_resource_return_error() {
        //Given
        viewModel.fetchResource(Params("owner", "project"))
        verify(useCase, times(1)).execute(captor.capture(), eq(Params("owner", "project")))
        //When
        captor.firstValue.onError(Exception())
        //Then
        Truth.assertThat(viewModel.getResourceResult().value).isInstanceOf(Error::class.java)
    }

    @Test
    fun should_fetch_resource_return_error_throwable() {
        //Given
        val expected = Exception("Nasty exception !")
        viewModel.fetchResource(Params("owner", "project"))
        verify(useCase, times(1)).execute(captor.capture(), eq(Params("owner", "project")))
        //When
        captor.firstValue.onError(expected)
        //Then
        Truth.assertThat((viewModel.getResourceResult().value as Error).throwable).isEqualTo(expected)
    }
}