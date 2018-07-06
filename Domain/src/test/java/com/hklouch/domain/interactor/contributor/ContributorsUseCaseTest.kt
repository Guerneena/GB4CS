package com.hklouch.domain.interactor.contributor

import com.hklouch.domain.interactor.contributor.ContributorsUseCase.Params
import com.hklouch.domain.repository.ProjectsRepository
import com.hklouch.domain.interactor.Data
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class ContributorsUseCaseTest {

    @Mock private lateinit var projectsRepository: ProjectsRepository

    private lateinit var useCase: ContributorsUseCase

    private val projectData = Data()

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        useCase = ContributorsUseCase(projectsRepository)
    }

    @Test
    fun should_search_complete() {
        //Given
        given(projectsRepository.getContributors("owner", "project", 2, 50))
                .willReturn(Observable.just(projectData.contributorList))
        //When
        useCase.buildUseCaseObservable(Params("owner", "project", 2, 50)).test()
                //Test
                .assertComplete()
    }

    @Test
    fun should_search_call_repository() {
        //Given
        given(projectsRepository.getContributors("owner", "project", 2, 50))
                .willReturn(Observable.just(projectData.contributorList))
        //When
        useCase.buildUseCaseObservable(Params("owner", "project", 2, 50)).test()
        //Test
        verify(projectsRepository).getContributors("owner", "project", 2, 50)
    }

    @Test
    fun should_search_return_data() {
        //Given
        given(projectsRepository.getContributors("owner", "project", 2, 50))
                .willReturn(Observable.just(projectData.contributorList))
        val expected = projectData.contributorList
        //When
        useCase.buildUseCaseObservable(Params("owner", "project", 2, 50)).test()
                //Test
                .assertValue(expected)
    }
}