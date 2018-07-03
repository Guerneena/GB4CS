package com.hklouch.utils

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity

/**
 * Get a viewModel that is bound to the Activity's lifecycle (meaning that it will survive config changes).
 * @param f the factory closure that will instantiate the ViewModel when it does not exist already.
 */
internal inline fun <reified T : ViewModel> AppCompatActivity.getViewModel(crossinline f: () -> T): T {
    return ViewModelProviders.of(this, factory(f)).get(T::class.java).also {
        if (it is LifecycleObserver) {
            lifecycle.addObserver(it)
        }
    }
}


@Suppress("UNCHECKED_CAST")
internal inline fun <VM : ViewModel> factory(crossinline f: () -> VM) = object : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return f() as T
    }
}
