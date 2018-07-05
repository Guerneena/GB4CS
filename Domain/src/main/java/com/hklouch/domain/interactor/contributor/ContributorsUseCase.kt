package com.hklouch.domain.interactor.contributor

import com.hklouch.domain.interactor.ObservableUseCase
import com.hklouch.domain.interactor.contributor.ContributorsUseCase.Params
import com.hklouch.domain.model.PagingWrapper
import com.hklouch.domain.model.User
import com.hklouch.domain.repository.ProjectsRepository
import io.reactivex.Observable
import javax.inject.Inject

open class ContributorsUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) : ObservableUseCase<PagingWrapper<User>, Params>() {

    override fun buildUseCaseObservable(params: Params?): Observable<PagingWrapper<User>> {
        if (params == null) throw IllegalArgumentException("Params can not be null")
        return projectsRepository.getContributors(params.ownerName,
                                                  params.projectName,
                                                  params.page,
                                                  params.resultsPerPage)
    }

    data class Params(val ownerName: String,
                      val projectName: String,
                      val page: Int? = null,
                      val resultsPerPage: Int? = null)
}