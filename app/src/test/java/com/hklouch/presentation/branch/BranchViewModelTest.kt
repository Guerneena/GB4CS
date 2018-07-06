package com.hklouch.presentation.branch

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.hklouch.domain.interactor.branch.BranchesUseCase
import com.hklouch.domain.interactor.branch.BranchesUseCase.Params
import com.hklouch.domain.model.Branch
import com.hklouch.domain.model.PagingWrapper
import com.hklouch.presentation.Data
import com.hklouch.ui.State.Error
import com.hklouch.ui.State.Loading
import com.hklouch.ui.State.Success
import com.hklouch.ui.model.toUiBranchItem
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

class BranchViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var branchesUseCase = mock<BranchesUseCase>()

    private lateinit var branchViewModel: BranchViewModel

    private val projectData = Data()

    @Captor
    private val captor = argumentCaptor<DisposableObserver<PagingWrapper<Branch>>>()

    @Before
    fun before() {
        branchViewModel = BranchViewModel(branchesUseCase, Branch::toUiBranchItem, null)
    }

    @Test
    fun should_instantiation_trigger_loading_state() {
        //When
        branchViewModel = BranchViewModel(branchesUseCase, Branch::toUiBranchItem, null)
        //Then
        Truth.assertThat(branchViewModel.getResourceResult().value).isInstanceOf(Loading::class.java)
    }

    @Test
    fun should_fetch_resource_on_instantiation() {
        //Then
        verify(branchesUseCase, times(1)).execute(any(), eq(null))
    }

    @Test
    fun should_fetch_resource_execute_use_case() {
        //When
        branchViewModel.fetchResource(Params("owner", "project"))
        //Then
        verify(branchesUseCase, times(1)).execute(any(), eq(Params("owner", "project")))
    }

    @Test
    fun should_fetch_resource_return_success() {
        //Given
        val branchList = projectData.domain.branchList
        branchViewModel.fetchResource(Params("owner", "project"))
        verify(branchesUseCase, times(1)).execute(captor.capture(), eq(Params("owner", "project")))
        //When
        captor.firstValue.onNext(branchList)
        //Then
        Truth.assertThat(branchViewModel.getResourceResult().value).isInstanceOf(Success::class.java)
    }

    @Test
    fun should_fetch_resource_return_data() {
        //Given
        val branchList = projectData.domain.branchList
        val expected = projectData.ui.uiBranchList
        branchViewModel.fetchResource(Params("owner", "project"))
        verify(branchesUseCase, times(1)).execute(captor.capture(), eq(Params("owner", "project")))
        //When
        captor.firstValue.onNext(branchList)
        //Then
        Truth.assertThat((branchViewModel.getResourceResult().value as Success).data).isEqualTo(expected)
    }

    @Test
    fun should_fetch_resource_return_error() {
        //Given
        branchViewModel.fetchResource(Params("owner", "project"))
        verify(branchesUseCase, times(1)).execute(captor.capture(), eq(Params("owner", "project")))
        //When
        captor.firstValue.onError(Exception())
        //Then
        Truth.assertThat(branchViewModel.getResourceResult().value).isInstanceOf(Error::class.java)
    }

    @Test
    fun should_fetch_resource_return_error_throwable() {
        //Given
        val expected = Exception("Nasty exception !")
        branchViewModel.fetchResource(Params("owner", "project"))
        verify(branchesUseCase, times(1)).execute(captor.capture(), eq(Params("owner", "project")))
        //When
        captor.firstValue.onError(expected)
        //Then
        Truth.assertThat((branchViewModel.getResourceResult().value as Error).throwable).isEqualTo(expected)
    }
}