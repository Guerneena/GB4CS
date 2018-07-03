package com.hklouch.utils

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

// View
fun View.hideKeyboard() {
    context.inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

val Context.inputMethodManager: InputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

val Activity.rootView
    get() = findViewById<ViewGroup>(android.R.id.content)

val Fragment.rootView
    get() = activity.findViewById<ViewGroup>(android.R.id.content)