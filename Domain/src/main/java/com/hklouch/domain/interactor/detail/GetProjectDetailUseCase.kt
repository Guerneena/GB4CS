package com.hklouch.domain.interactor.detail

import com.hklouch.domain.interactor.ObservableUseCase
import com.hklouch.domain.interactor.detail.GetProjectDetailUseCase.Params
import com.hklouch.domain.model.Project
import com.hklouch.domain.repository.ProjectsRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetProjectDetailUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) : ObservableUseCase<Project, Params>() {

    override fun buildUseCaseObservable(params: Params?): Observable<Project> {
        if (params == null) throw IllegalArgumentException("Params can not be null")
        return projectsRepository.getProject(params.ownerName, params.projectName)
    }

    data class Params(val ownerName: String, val projectName: String)
}