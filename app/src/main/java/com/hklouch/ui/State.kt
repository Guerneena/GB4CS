package com.hklouch.ui

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