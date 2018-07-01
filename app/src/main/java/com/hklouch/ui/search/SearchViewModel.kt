package com.hklouch.ui.search

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.hklouch.domain.interactor.search.SearchProjectsUseCase
import com.hklouch.domain.model.Project
import com.hklouch.ui.State
import com.hklouch.ui.loading
import com.hklouch.ui.model.UiProjectItem
import com.hklouch.ui.model.toUiProjectItem
import com.hklouch.ui.toState
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchViewModelFactory @Inject constructor(private val searchProjectsUseCase: SearchProjectsUseCase) {
    fun supply() = SearchViewModel(searchProjectsUseCase)
}

class SearchViewModel(private val searchProjectsUseCase: SearchProjectsUseCase) : ViewModel() {


    private val liveData: MutableLiveData<State<List<UiProjectItem>>> = MutableLiveData()

    override fun onCleared() {
        searchProjectsUseCase.dispose()
        super.onCleared()
    }

    fun getResultRepos() = liveData


    fun searchRepos(query: String) {
        if (query.isBlank()) return
        liveData.postValue(loading())
        searchProjectsUseCase.execute(ProjectsSubscriber(), SearchProjectsUseCase.Params.forSearch(query))
    }

    inner class ProjectsSubscriber : DisposableObserver<List<Project>>() {
        override fun onNext(result: List<Project>) {
            liveData.postValue(result.map { it.toUiProjectItem() }.toState())
        }

        override fun onComplete() {}

        override fun onError(e: Throwable) {
            liveData.postValue(e.toState())
        }

    }
}