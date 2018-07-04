package com.hklouch.domain.interactor.browse

import com.hklouch.domain.interactor.ObservableUseCase
import com.hklouch.domain.interactor.browse.GetProjectsUseCase.Params
import com.hklouch.domain.model.ProjectList
import com.hklouch.domain.repository.ProjectsRepository
import io.reactivex.Observable
import javax.inject.Inject

open class GetProjectsUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) : ObservableUseCase<ProjectList, Params?>() {

    override fun buildUseCaseObservable(params: Params?): Observable<ProjectList> {
        return projectsRepository.getProjects(params?.next)
    }

    data class Params(val next: Int?)
}