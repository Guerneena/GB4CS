package com.hklouch.ui.browse

import android.arch.lifecycle.MutableLiveData
import com.hklouch.domain.model.PagingWrapper
import com.hklouch.ui.State
import com.hklouch.ui.model.UiPagingWrapper
import com.hklouch.ui.model.toUiPagingWrapper
import com.hklouch.ui.toState
import io.reactivex.observers.DisposableObserver
import kotlin.reflect.KFunction1

/**
 * A [PagingWrapper]<[T]> observer that maps events it receives to [State]<[R]>
 * models and post them to the provided [liveDataObserver]
 *
 * @param liveDataObserver events received will be posted to [MutableLiveData]
 */
class ResourceObserver<T, R>(private val liveDataObserver: MutableLiveData<State<UiPagingWrapper<R>>>,
                             private val mapper: KFunction1<T, R>) : DisposableObserver<PagingWrapper<T>>() {

    override fun onNext(result: PagingWrapper<T>) {
        liveDataObserver.postValue(result.toUiPagingWrapper(mapper).toState())
    }

    override fun onComplete() {}

    override fun onError(e: Throwable) {
        liveDataObserver.postValue(e.toState())
    }

}