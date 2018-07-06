package com.hklouch.githubrepos4cs.domain.interactor.browse

import com.hklouch.domain.interactor.browse.GetProjectsUseCase
import com.hklouch.domain.interactor.browse.GetProjectsUseCase.Params
import com.hklouch.domain.repository.ProjectsRepository
import com.hklouch.githubrepos4cs.domain.interactor.Data
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class GetProjectsUseCaseTest {

    @Mock private lateinit var projectsRepository: ProjectsRepository

    private lateinit var getProjectsUseCase: GetProjectsUseCase

    private val projectData = Data()

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        getProjectsUseCase = GetProjectsUseCase(projectsRepository)
    }

    @Test
    fun should_search_complete() {
        //Given
        given(projectsRepository.getProjects(321))
                .willReturn(Observable.just(projectData.projectList))
        //When
        getProjectsUseCase.buildUseCaseObservable(Params(321)).test()
                //Test
                .assertComplete()
    }

    @Test
    fun should_search_call_repository() {
        //Given
        given(projectsRepository.getProjects(321))
                .willReturn(Observable.just(projectData.projectList))
        //When
        getProjectsUseCase.buildUseCaseObservable(Params(321)).test()
        //Test
        verify(projectsRepository).getProjects(321)
    }

    @Test
    fun should_search_return_data() {
        //Given
        given(projectsRepository.getProjects(321))
                .willReturn(Observable.just(projectData.projectList))
        val expected = projectData.projectList
        //When
        getProjectsUseCase.buildUseCaseObservable(Params(321)).test()
                //Test
                .assertValue(expected)
    }

}