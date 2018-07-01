package com.hklouch.domain.ext.rx

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers as RxSchedulers

object GithubSchedulers {

    private lateinit var io: Scheduler
    private lateinit var computation: Scheduler
    private lateinit var trampoline: Scheduler
    private lateinit var mainThread: Scheduler

    fun init(computation: Scheduler = RxSchedulers.computation(),
             io: Scheduler = RxSchedulers.io(),
             trampoline: Scheduler = RxSchedulers.trampoline(),
             mainThread: Scheduler = RxSchedulers.trampoline()) {

        GithubSchedulers.io = io
        GithubSchedulers.computation = computation
        GithubSchedulers.trampoline = trampoline
        GithubSchedulers.mainThread = mainThread

    }

    fun computation() = computation
    fun io() = io
    fun trampoline() = trampoline
    fun mainThread() = mainThread
}

fun GithubSchedulers.initForTests() {
    init(io = RxSchedulers.trampoline(),
         computation = RxSchedulers.trampoline(),
         mainThread = RxSchedulers.trampoline())
}
