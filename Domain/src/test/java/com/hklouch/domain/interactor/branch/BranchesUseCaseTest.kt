package com.hklouch.domain.interactor.branch

import com.hklouch.domain.interactor.branch.BranchesUseCase.Params
import com.hklouch.domain.repository.ProjectsRepository
import com.hklouch.domain.interactor.Data
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class BranchesUseCaseTest {

    @Mock private lateinit var projectsRepository: ProjectsRepository

    private lateinit var branchesUseCase: BranchesUseCase

    private val projectData = Data()

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        branchesUseCase = BranchesUseCase(projectsRepository)
    }

    @Test
    fun should_search_complete() {
        //Given
        given(projectsRepository.getBranches("owner", "project", 2, 50))
                .willReturn(Observable.just(projectData.branchList))
        //When
        branchesUseCase.buildUseCaseObservable(Params("owner", "project", 2, 50)).test()
                //Test
                .assertComplete()
    }

    @Test
    fun should_search_call_repository() {
        //Given
        given(projectsRepository.getBranches("owner", "project", 2, 50))
                .willReturn(Observable.just(projectData.branchList))
        //When
        branchesUseCase.buildUseCaseObservable(Params("owner", "project", 2, 50)).test()
        //Test
        verify(projectsRepository).getBranches("owner", "project", 2, 50)
    }

    @Test
    fun should_search_return_data() {
        //Given
        given(projectsRepository.getBranches("owner", "project", 2, 50))
                .willReturn(Observable.just(projectData.branchList))
        val expected = projectData.branchList
        //When
        branchesUseCase.buildUseCaseObservable(Params("owner", "project", 2, 50)).test()
                //Test
                .assertValue(expected)
    }
}