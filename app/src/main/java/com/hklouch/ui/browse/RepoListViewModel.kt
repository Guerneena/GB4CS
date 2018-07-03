package com.hklouch.ui.browse

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.hklouch.domain.interactor.browse.GetProjectsUseCase
import com.hklouch.domain.model.ProjectList
import com.hklouch.ui.State
import com.hklouch.ui.loading
import com.hklouch.ui.model.UiPagingModel
import com.hklouch.ui.model.toUiPagingModel
import com.hklouch.ui.toState
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoListViewModelFactory @Inject constructor(private val getProjectsUseCase: GetProjectsUseCase) {

    fun supply() = RepoListViewModel(getProjectsUseCase)
}

class RepoListViewModel(private val getProjectsUseCase: GetProjectsUseCase) : ViewModel() {


    private val liveData: MutableLiveData<State<UiPagingModel>> = MutableLiveData()

    init {
        fetchPublicRepos()
    }

    override fun onCleared() {
        getProjectsUseCase.dispose()
        super.onCleared()
    }

    fun getPublicRepos() = liveData


    fun fetchPublicRepos(since: Int? = null) {
        liveData.postValue(loading())
        getProjectsUseCase.execute(ProjectsSubscriber(), GetProjectsUseCase.Params(since))
    }

    inner class ProjectsSubscriber : DisposableObserver<ProjectList>() {
        override fun onNext(result: ProjectList) {
            liveData.postValue(result.toUiPagingModel().toState())
        }

        override fun onComplete() {}

        override fun onError(e: Throwable) {
            liveData.postValue(e.toState())
        }

    }
}