package com.hklouch.domain.ext.rx

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

//operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
//    add(disposable)
//}

fun <T> Observable<T>.safeSubscribe(): Disposable = subscribe({}, {})
fun <T> Observable<T>.safeSubscribe(onNext: (T) -> Unit): Disposable = subscribe(onNext, {})
fun Completable.safeSubscribe(): Disposable = subscribe({}, {})
fun Completable.safeSubscribe(onComplete: () -> Unit): Disposable = subscribe(onComplete, {})
fun <T> Single<T>.safeSubscribe(onSuccess: (T) -> Unit): Disposable = subscribe(onSuccess, {})

