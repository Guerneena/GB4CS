package com.hklouch.domain.interactor.search

import com.hklouch.domain.interactor.ObservableUseCase
import com.hklouch.domain.interactor.search.SearchProjectsUseCase.Params
import com.hklouch.domain.model.Project
import com.hklouch.domain.repository.ProjectsRepository
import io.reactivex.Observable
import javax.inject.Inject

class SearchProjectsUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) : ObservableUseCase<List<Project>, Params>() {

    override fun buildUseCaseObservable(params: Params?): Observable<List<Project>> {
        if (params == null) throw IllegalArgumentException("Params can not be null")
        return projectsRepository.searchProjects(params.query)
    }

    data class Params(val query: String) {
        companion object {
            fun forSearch(query: String): Params {
                return Params(query)
            }
        }
    }
}