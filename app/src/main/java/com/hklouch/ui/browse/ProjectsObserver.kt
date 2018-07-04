package com.hklouch.ui.browse

import android.arch.lifecycle.MutableLiveData
import com.hklouch.domain.model.ProjectList
import com.hklouch.ui.State
import com.hklouch.ui.model.UiPagingModel
import com.hklouch.ui.model.toUiPagingModel
import com.hklouch.ui.toState
import io.reactivex.observers.DisposableObserver

/**
 * A [ProjectList] observer that maps events it receives to [State]<[UiPagingModel]>
 * models and post them to the provided [liveData]
 *
 * @param liveData events received will be posted to this parameter
 */
class ProjectsObserver(private val liveData: MutableLiveData<State<UiPagingModel>>) : DisposableObserver<ProjectList>() {

    override fun onNext(result: ProjectList) {
        liveData.postValue(result.toUiPagingModel().toState())
    }

    override fun onComplete() {}

    override fun onError(e: Throwable) {
        liveData.postValue(e.toState())
    }

}