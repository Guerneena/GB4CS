package com.hklouch.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.hklouch.domain.interactor.ObservableUseCase
import com.hklouch.domain.model.PagingWrapper
import com.hklouch.ui.browse.ResourceObserver
import com.hklouch.ui.model.UiPagingWrapper
import kotlin.reflect.KFunction1
import kotlin.reflect.KProperty1

class ResourceListViewModelFactory<T, R, H : ObservableUseCase<PagingWrapper<T>, Params>, Params>
constructor(private val useCase: H,
            private val mapper: KFunction1<T, R>) {

    fun supply(params: Params? = null) = ResourceListViewModel(useCase, mapper, params)
}

class ResourceListViewModel<T, R, Params>(private val useCase: ObservableUseCase<PagingWrapper<T>, Params>,
                                          private val mapper: KFunction1<T, R>,
                                          params: Params?) : ViewModel() {

    private val liveData: MutableLiveData<State<UiPagingWrapper<R>>> = MutableLiveData()

    private var lastParams: Params? = null

    init {
        fetchResource(params)
    }

    override fun onCleared() {
        useCase.dispose()
        super.onCleared()
    }

    fun getResourceResult() = liveData

    fun fetchResource(params: Params? = null) {
        liveData.postValue(loading())
        lastParams = params
        useCase.execute(ResourceObserver(liveDataObserver = liveData, mapper = mapper), params)
    }

    fun lastQuery(): Params? = lastParams
}