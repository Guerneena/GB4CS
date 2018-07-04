package com.hklouch.domain.interactor

import com.hklouch.domain.ext.rx.GithubSchedulers
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.plusAssign

abstract class SingleUseCase<T, in Params> {

    private val disposables = CompositeDisposable()

    abstract fun buildUseCaseObservable(params: Params? = null): Single<T>

    open fun execute(observer: DisposableSingleObserver<T>, params: Params?) {
        disposables += this.buildUseCaseObservable(params)
                .subscribeOn(GithubSchedulers.io())
                .observeOn(GithubSchedulers.mainThread())
                .subscribeWith(observer)
    }

    fun dispose() {
        disposables.clear()
    }
}