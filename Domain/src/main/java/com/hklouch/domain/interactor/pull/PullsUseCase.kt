package com.hklouch.domain.interactor.pull

import com.hklouch.domain.interactor.ObservableUseCase
import com.hklouch.domain.interactor.pull.PullsUseCase.Params
import com.hklouch.domain.model.PagingWrapper
import com.hklouch.domain.model.Pull
import com.hklouch.domain.repository.ProjectsRepository
import io.reactivex.Observable
import javax.inject.Inject

open class PullsUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) : ObservableUseCase<PagingWrapper<Pull>, Params>() {

    override fun buildUseCaseObservable(params: Params?): Observable<PagingWrapper<Pull>> {
        if (params == null) throw IllegalArgumentException("Params can not be null")
        return projectsRepository.getPulls(params.ownerName,
                                           params.projectName,
                                           params.state,
                                           params.page,
                                           params.resultsPerPage)
    }

    data class Params(val ownerName: String,
                      val projectName: String,
                      val state: String? = null,
                      val page: Int? = null,
                      val resultsPerPage: Int? = null)
}