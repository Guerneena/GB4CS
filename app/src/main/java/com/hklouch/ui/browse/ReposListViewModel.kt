package com.hklouch.ui.browse

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.hklouch.domain.interactor.browse.GetProjectsUseCase
import com.hklouch.ui.State
import com.hklouch.ui.loading
import com.hklouch.ui.model.UiPagingModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReposListViewModelFactory @Inject constructor(private val getProjectsUseCase: GetProjectsUseCase) {

    fun supply() = ReposListViewModel(getProjectsUseCase)
}

class ReposListViewModel(private val getProjectsUseCase: GetProjectsUseCase) : ViewModel() {

    private val liveData: MutableLiveData<State<UiPagingModel>> = MutableLiveData()

    init {
        fetchRepos()
    }

    override fun onCleared() {
        getProjectsUseCase.dispose()
        super.onCleared()
    }

    fun getResultRepos() = liveData


    fun fetchRepos(since: Int? = null) {
        liveData.postValue(loading())
        getProjectsUseCase.execute(ProjectsObserver(liveData), GetProjectsUseCase.Params(since))
    }
}