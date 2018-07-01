package com.hklouch.app

import android.app.Activity
import android.app.Application
import com.hklouch.di.DaggerAppComponent
import com.hklouch.domain.ext.rx.GithubSchedulers
import com.hklouch.githubrepos4cs.BuildConfig
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

open class GithubRepos4csApplication : Application(), HasActivityInjector {

    @Inject lateinit var androidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> {
        return androidInjector
    }


    override fun onCreate() {
        super.onCreate()

        GithubSchedulers.init(mainThread = AndroidSchedulers.mainThread())

        if (BuildConfig.DEBUG) Timber.plant(DebugTree())

        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
    }


}