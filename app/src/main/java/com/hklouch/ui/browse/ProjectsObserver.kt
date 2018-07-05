package com.hklouch.ui.browse

import android.arch.lifecycle.MutableLiveData
import com.hklouch.domain.model.PagingWrapper
import com.hklouch.domain.model.Project
import com.hklouch.ui.State
import com.hklouch.ui.model.UiPagingModel
import com.hklouch.ui.model.toUiPagingModel
import com.hklouch.ui.toState
import io.reactivex.observers.DisposableObserver

/**
 * A [PagingWrapper]<[Project]> observer that maps events it receives to [State]<[UiPagingModel]>
 * models and post them to the provided [liveData]
 *
 * @param liveData events received will be posted to this parameter
 */
class ProjectsObserver(private val liveData: MutableLiveData<State<UiPagingModel>>) : DisposableObserver<PagingWrapper<Project>>() {

    override fun onNext(result: PagingWrapper<Project>) {
        liveData.postValue(result.toUiPagingModel().toState())
    }

    override fun onComplete() {}

    override fun onError(e: Throwable) {
        liveData.postValue(e.toState())
    }

}