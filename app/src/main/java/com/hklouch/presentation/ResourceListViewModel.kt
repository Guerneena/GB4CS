package com.hklouch.presentation

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.hklouch.domain.interactor.ObservableUseCase
import com.hklouch.domain.model.PagingWrapper
import com.hklouch.ui.State
import com.hklouch.ui.loading
import com.hklouch.ui.model.UiPagingWrapper
import kotlin.reflect.KFunction1

/**
 * A base class for ViewModel factory
 *
 * @param T type of resource being fetched by the viewModel
 * @param R type of resource being returned by the viewModel
 * @param Params type of params being used to fetch data
 */
abstract class ResourceListViewModelFactory<T, R, Params> {

    abstract fun supply(params: Params? = null): ResourceListViewModel<T, R, Params>
}

/**
 * A base class for ViewModel
 *
 * @param useCase the [ObservableUseCase] used to fetch data
 * @param mapper a kotlin function used to map data from type [T] returned
 * from [ObservableUseCase] to type [R], expected by [ResourceObserver] consumers
 *
 * @see [ResourceListViewModelFactory]
 */
abstract class ResourceListViewModel<T, R, Params>(private val useCase: ObservableUseCase<PagingWrapper<T>, Params>,
                                                   private val mapper: KFunction1<T, R>,
                                                   params: Params?,
                                                   needsFetchOnStart: Boolean = true) : ViewModel() {

    private val liveData: MutableLiveData<State<UiPagingWrapper<R>>> = MutableLiveData()

    private var lastParams: Params? = null

    init {
        if (needsFetchOnStart) fetchResource(params)
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