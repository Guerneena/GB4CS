package com.hklouch.ui.search

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.hklouch.domain.interactor.search.SearchProjectsUseCase
import com.hklouch.domain.model.Project
import com.hklouch.ui.State
import com.hklouch.ui.browse.ResourceObserver
import com.hklouch.ui.loading
import com.hklouch.ui.model.UiPagingWrapper
import com.hklouch.ui.model.UiProjectPreviewItem
import com.hklouch.ui.model.toUiProjectPreviewItem
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

    private val liveData: MutableLiveData<State<UiPagingWrapper<UiProjectPreviewItem>>> = MutableLiveData()
    private val querySubject = BehaviorSubject.create<SearchProjectsUseCase.Params>()

    private val disposable = CompositeDisposable()

    init {
        disposable += querySubject.subscribe {
            searchProjectsUseCase.execute(ResourceObserver(liveDataObserver = liveData,
                                                           mapper = Project::toUiProjectPreviewItem), it)
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