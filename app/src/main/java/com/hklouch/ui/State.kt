package com.hklouch.ui

import io.reactivex.Observable
import timber.log.Timber

sealed class State<T>(val loading: Boolean = false,
                      val success: Boolean = false,
                      val error: Boolean = false) {

    class Loading<T> : State<T>(loading = true)
    class Error<T>(val throwable: Throwable) : State<T>(error = true)
    class Success<T>(val data: T) : State<T>(success = true)
}

fun <T> T.toState(): State<T> = State.Success(this)

fun <T> Throwable.toState(): State<T> = State.Error(this)
fun <T> loading(): State<T> = State.Loading()


fun <T> Observable<T>.mapState(): Observable<State<T>> = map { item -> item.toState() }
        .startWith(loading())
        .onErrorResumeNext { throwable: Throwable ->
            Timber.w(throwable)
            Observable.just(throwable.toState())
        }