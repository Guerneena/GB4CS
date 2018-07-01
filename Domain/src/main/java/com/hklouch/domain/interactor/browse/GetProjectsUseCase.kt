package com.hklouch.domain.interactor.browse

import com.hklouch.domain.interactor.ObservableUseCase
import com.hklouch.domain.interactor.browse.GetProjectsUseCase.Params
import com.hklouch.domain.model.Project
import com.hklouch.domain.repository.ProjectsRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetProjectsUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) : ObservableUseCase<List<Project>, Params?>() {

    override fun buildUseCaseObservable(params: Params?): Observable<List<Project>> {
        return projectsRepository.getProjects(params?.next)
    }

    data class Params(val next: String?) {
        companion object {
            fun forGetProjects(next: String?): Params {
                return Params(next)
            }
        }
    }
}