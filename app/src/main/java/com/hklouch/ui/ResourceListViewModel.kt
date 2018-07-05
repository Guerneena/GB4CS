package com.hklouch.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.hklouch.domain.interactor.ObservableUseCase
import com.hklouch.domain.model.PagingWrapper
import com.hklouch.ui.browse.ResourceObserver
import com.hklouch.ui.model.UiPagingWrapper
import kotlin.reflect.KFunction1

class ResourceListViewModelFactory<T, R, H : ObservableUseCase<PagingWrapper<T>, Params?>, Params>
constructor(private val useCase: H,
            private val mapper: KFunction1<T, R>) {

    fun supply() = ResourceListViewModel(useCase, mapper)
}

class ResourceListViewModel<T, R, in Params>(private val useCase: ObservableUseCase<PagingWrapper<T>, Params?>,
                                             private val mapper: KFunction1<T, R>) : ViewModel() {

    private val liveData: MutableLiveData<State<UiPagingWrapper<R>>> = MutableLiveData()

    init {
        fetchResource()
    }

    override fun onCleared() {
        useCase.dispose()
        super.onCleared()
    }

    fun getResourceResult() = liveData


    fun fetchResource(params: Params? = null) {
        liveData.postValue(loading())
        useCase.execute(ResourceObserver(liveDataObserver = liveData, mapper = mapper), params)
    }
}