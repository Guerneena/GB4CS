package com.hklouch.ui.search

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.hklouch.domain.interactor.search.SearchProjectsUseCase
import com.hklouch.domain.model.ProjectList
import com.hklouch.ui.State
import com.hklouch.ui.loading
import com.hklouch.ui.model.UiPagingModel
import com.hklouch.ui.model.toUiPagingModel
import com.hklouch.ui.toState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchViewModelFactory @Inject constructor(private val searchProjectsUseCase: SearchProjectsUseCase) {
    fun supply() = SearchViewModel(searchProjectsUseCase)
}

class SearchViewModel(private val searchProjectsUseCase: SearchProjectsUseCase) : ViewModel() {


    private val liveData: MutableLiveData<State<UiPagingModel>> = MutableLiveData()
    private val querySubject = BehaviorSubject.create<SearchProjectsUseCase.Params>()

    private val disposable = CompositeDisposable()

    init {
        disposable += querySubject.subscribe {
            searchProjectsUseCase.execute(ProjectsSubscriber(), it)
        }
    }

    override fun onCleared() {
        searchProjectsUseCase.dispose()
        disposable.clear()
        super.onCleared()
    }

    fun getResultRepos() = liveData

    fun submitQuery(query: String) {
        if (query.isBlank()) return
        liveData.postValue(loading())
        querySubject.onNext(SearchProjectsUseCase.Params(query))
    }

    fun requestNextPage(next: Int) {
        liveData.postValue(loading())
        querySubject.onNext(querySubject.value.copy(nextPage = next))
    }

    fun retry() {
        querySubject.onNext(querySubject.value)
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