package com.hklouch.ui.detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.hklouch.domain.interactor.detail.GetProjectDetailUseCase
import com.hklouch.domain.interactor.detail.GetProjectDetailUseCase.Params
import com.hklouch.domain.model.Project
import com.hklouch.ui.State
import com.hklouch.ui.loading
import com.hklouch.ui.model.UiProjectItem
import com.hklouch.ui.model.toUiProjectItem
import com.hklouch.ui.toState
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectViewModelFactory @Inject constructor(private val getProjectDetailUseCase: GetProjectDetailUseCase) {
    fun supply(ownerName: String, projectName: String) = ProjectViewModel(getProjectDetailUseCase, ownerName, projectName)
}

class ProjectViewModel(private val getProjectDetailUseCase: GetProjectDetailUseCase,
                       ownerName: String,
                       projectName: String) : ViewModel() {


    private val liveData: MutableLiveData<State<UiProjectItem>> = MutableLiveData()

    init {
        getProjectDetail(ownerName, projectName)
    }

    override fun onCleared() {
        getProjectDetailUseCase.dispose()
        super.onCleared()
    }

    fun getResultProjectDetail() = liveData

    fun getProjectDetail(ownerName: String, projectName: String) {
        liveData.postValue(loading())
        getProjectDetailUseCase.execute(ProjectDetailSubscriber(), Params(ownerName, projectName))
    }

    inner class ProjectDetailSubscriber : DisposableSingleObserver<Project>() {

        override fun onSuccess(result: Project) {
            liveData.postValue(result.toUiProjectItem().toState())
        }

        override fun onError(e: Throwable) {
            liveData.postValue(e.toState())
        }

    }
}