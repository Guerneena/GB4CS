package com.hklouch.domain.interactor.issue

import com.hklouch.domain.interactor.ObservableUseCase
import com.hklouch.domain.interactor.issue.IssuesUseCase.Params
import com.hklouch.domain.model.Issue
import com.hklouch.domain.model.PagingWrapper
import com.hklouch.domain.repository.ProjectsRepository
import io.reactivex.Observable
import javax.inject.Inject

open class IssuesUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) : ObservableUseCase<PagingWrapper<Issue>, Params>() {

    override fun buildUseCaseObservable(params: Params?): Observable<PagingWrapper<Issue>> {
        if (params == null) throw IllegalArgumentException("Params can not be null")
        return projectsRepository.getIssues(params.ownerName,
                                            params.projectName,
                                            params.page,
                                            params.resultsPerPage)
    }

    data class Params(val ownerName: String,
                      val projectName: String,
                      val page: Int? = null,
                      val resultsPerPage: Int? = null)
}