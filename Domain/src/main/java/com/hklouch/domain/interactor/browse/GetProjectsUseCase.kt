package com.hklouch.domain.interactor.browse

import com.hklouch.domain.interactor.ObservableUseCase
import com.hklouch.domain.interactor.browse.GetProjectsUseCase.Params
import com.hklouch.domain.model.PagingWrapper
import com.hklouch.domain.model.Project
import com.hklouch.domain.repository.ProjectsRepository
import io.reactivex.Observable
import javax.inject.Inject

open class GetProjectsUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) : ObservableUseCase<PagingWrapper<Project>, Params?>() {

    override fun buildUseCaseObservable(params: Params?): Observable<PagingWrapper<Project>> {
        return projectsRepository.getProjects(params?.next)
    }

    data class Params(val next: Int?)
}