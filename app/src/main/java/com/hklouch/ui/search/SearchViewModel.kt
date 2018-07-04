package com.hklouch.ui.search

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.hklouch.domain.interactor.search.SearchProjectsUseCase
import com.hklouch.ui.State
import com.hklouch.ui.browse.ProjectsObserver
import com.hklouch.ui.loading
import com.hklouch.ui.model.UiPagingModel
import io.reactivex.disposables.CompositeDisposable
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
            searchProjectsUseCase.execute(ProjectsObserver(liveData), it)
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
        querySubject.value?.let {
            liveData.postValue(loading())
            querySubject.onNext(it.copy(nextPage = next))
        }
    }

    fun retry() {
        querySubject.value?.let {
            querySubject.onNext(it)
        }
    }
}