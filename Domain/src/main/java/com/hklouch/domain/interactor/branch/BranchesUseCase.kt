package com.hklouch.domain.interactor.branch

import com.hklouch.domain.interactor.ObservableUseCase
import com.hklouch.domain.interactor.branch.BranchesUseCase.Params
import com.hklouch.domain.model.Branch
import com.hklouch.domain.model.PagingWrapper
import com.hklouch.domain.repository.ProjectsRepository
import io.reactivex.Observable
import javax.inject.Inject

open class BranchesUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) :
        ObservableUseCase<PagingWrapper<Branch>, Params?>() {

    override fun buildUseCaseObservable(params: Params?): Observable<PagingWrapper<Branch>> {
        if (params == null) throw IllegalArgumentException("Params can not be null")
        return projectsRepository.getBranches(params.ownerName,
                                              params.projectName,
                                              params.page,
                                              params.resultsPerPage)
    }

    data class Params(val ownerName: String,
                      val projectName: String,
                      val page: Int? = null,
                      val resultsPerPage: Int? = null)
}