package com.hklouch.domain.interactor.search

import com.hklouch.domain.interactor.ObservableUseCase
import com.hklouch.domain.interactor.search.SearchProjectsUseCase.Params
import com.hklouch.domain.model.ProjectList
import com.hklouch.domain.repository.ProjectsRepository
import io.reactivex.Observable
import javax.inject.Inject

class SearchProjectsUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) : ObservableUseCase<ProjectList, Params>() {

    override fun buildUseCaseObservable(params: Params?): Observable<ProjectList> {
        if (params == null) throw IllegalArgumentException("Params can not be null")
        return projectsRepository.searchProjects(params.query, params.nextPage, params.resultsPerPage)
    }

    data class Params(val query: String,
                      val nextPage: Int? = null ,
                      val resultsPerPage: Int? = null)
}