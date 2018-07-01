package com.hklouch.domain.interactor

import com.hklouch.domain.ext.rx.GithubSchedulers
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.rxkotlin.plusAssign

abstract class ObservableUseCase<T, in Params> {

    private val disposables = CompositeDisposable()

    abstract fun buildUseCaseObservable(params: Params? = null): Observable<T>

    open fun execute(observer: DisposableObserver<T>, params: Params?) {
        disposables += this.buildUseCaseObservable(params)
                .subscribeOn(GithubSchedulers.io())
                .observeOn(GithubSchedulers.mainThread())
                .subscribeWith(observer)
    }

    fun dispose() {
        disposables.clear()
    }
}