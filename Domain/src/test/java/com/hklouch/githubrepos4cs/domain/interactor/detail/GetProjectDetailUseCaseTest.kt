package com.hklouch.githubrepos4cs.domain.interactor.detail

import com.hklouch.domain.interactor.detail.GetProjectDetailUseCase
import com.hklouch.domain.interactor.detail.GetProjectDetailUseCase.Params
import com.hklouch.domain.repository.ProjectsRepository
import com.hklouch.githubrepos4cs.domain.interactor.Data
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class GetProjectDetailUseCaseTest {

    @Mock private lateinit var projectsRepository: ProjectsRepository

    private lateinit var getProjectDetailUseCase: GetProjectDetailUseCase

    private val projectData = Data()

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        getProjectDetailUseCase = GetProjectDetailUseCase(projectsRepository)
    }

    @Test
    fun should_get_project_detail_complete() {
        //Given
        given(projectsRepository.getProject("owner", "project"))
                .willReturn(Single.just(projectData.project))
        //When
        getProjectDetailUseCase.buildUseCaseObservable(Params("owner", "project")).test()
                //Test
                .assertComplete()
    }

    @Test
    fun should_get_project_detail_call_repository() {
        //Given
        given(projectsRepository.getProject("owner", "project"))
                .willReturn(Single.just(projectData.project))
        //When
        getProjectDetailUseCase.buildUseCaseObservable(Params("owner", "project")).test()
        //Test
        verify(projectsRepository).getProject("owner", "project")
    }


    @Test
    fun should_get_project_detail_return_data() {
        //Given
        given(projectsRepository.getProject("owner", "project"))
                .willReturn(Single.just(projectData.project))
        val expected = projectData.project
        //When
        getProjectDetailUseCase.buildUseCaseObservable(Params("owner", "project")).test()
                //Test
                .assertValue(expected)
    }

}