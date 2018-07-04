package com.hklouch.githubrepos4cs.domain.interactor.search

import com.hklouch.domain.interactor.search.SearchProjectsUseCase
import com.hklouch.domain.interactor.search.SearchProjectsUseCase.Params
import com.hklouch.domain.repository.ProjectsRepository
import com.hklouch.githubrepos4cs.domain.interactor.Data
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class SearchProjectsUseCaseTest {

    @Mock private lateinit var projectsRepository: ProjectsRepository

    private lateinit var searchProjectsUseCase: SearchProjectsUseCase

    private val projectData = Data()

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        searchProjectsUseCase = SearchProjectsUseCase(projectsRepository)
    }

    @Test
    fun should_search_complete() {
        //Given
        given(projectsRepository.searchProjects("q", null, null))
                .willReturn(Observable.just(projectData.projectList))
        //When
        searchProjectsUseCase.buildUseCaseObservable(Params("q")).test()
                //Test
                .assertComplete()
    }

    @Test
    fun should_search_call_repository() {
        //Given
        given(projectsRepository.searchProjects("q", null, null))
                .willReturn(Observable.just(projectData.projectList))
        //When
        searchProjectsUseCase.buildUseCaseObservable(Params("q")).test()
        //Test
        verify(projectsRepository).searchProjects("q", null, null)
    }


    @Test
    fun should_search_return_data() {
        //Given
        given(projectsRepository.searchProjects("q", null, null))
                .willReturn(Observable.just(projectData.projectList))
        val expected = projectData.projectList
        //When
        searchProjectsUseCase.buildUseCaseObservable(Params("q")).test()
                //Test
                .assertValue(expected)
    }

}