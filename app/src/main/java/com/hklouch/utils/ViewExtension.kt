package com.hklouch.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

// View
fun View.hideKeyboard() {
    context.inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

val Context.inputMethodManager: InputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager