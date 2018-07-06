package com.hklouch.domain.interactor.pull

import com.hklouch.domain.interactor.pull.PullsUseCase.Params
import com.hklouch.domain.interactor.pull.PullsUseCase.Params.State.CLOSED
import com.hklouch.domain.interactor.pull.PullsUseCase.Params.State.OPEN
import com.hklouch.domain.repository.ProjectsRepository
import com.hklouch.domain.interactor.Data
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class PullsUseCaseTest {

    @Mock private lateinit var projectsRepository: ProjectsRepository

    private lateinit var useCase: PullsUseCase

    private val projectData = Data()

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        useCase = PullsUseCase(projectsRepository)
    }

    @Test
    fun should_search_complete() {
        //Given
        given(projectsRepository.getPulls("owner", "project", OPEN, 2, 50))
                .willReturn(Observable.just(projectData.pullList))
        //When
        useCase.buildUseCaseObservable(Params("owner", "project", OPEN,2, 50)).test()
                //Test
                .assertComplete()
    }

    @Test
    fun should_search_call_repository() {
        //Given
        given(projectsRepository.getPulls("owner", "project", OPEN,2, 50))
                .willReturn(Observable.just(projectData.pullList))
        //When
        useCase.buildUseCaseObservable(Params("owner", "project", OPEN,2, 50)).test()
        //Test
        verify(projectsRepository).getPulls("owner", "project", OPEN,2, 50)
    }

    @Test
    fun should_search_return_data() {
        //Given
        given(projectsRepository.getPulls("owner", "project", CLOSED,2, 50))
                .willReturn(Observable.just(projectData.pullList))
        val expected = projectData.pullList
        //When
        useCase.buildUseCaseObservable(Params("owner", "project", CLOSED,2, 50)).test()
                //Test
                .assertValue(expected)
    }
}