package com.hklouch.utils

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView

// View
fun View.hideKeyboard() {
    context.inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide(isGone: Boolean = true) {
    visibility = if (isGone) {
        View.GONE
    } else {
        View.INVISIBLE
    }
}

val Context.inputMethodManager: InputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

val Activity.rootView
    get() = findViewById<ViewGroup>(android.R.id.content)

val Fragment.rootView
    get() = activity.findViewById<ViewGroup>(android.R.id.content)

fun Context.getColorCompat(resId: Int) = ContextCompat.getColor(this, resId)

var TextView.drawableTop: Drawable?
    get() = compoundDrawables[1]
    set(value) {
        val compoundDrawables = compoundDrawables
        setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], value, compoundDrawables[2], compoundDrawables[3])
    }

fun Drawable.tint(color: Int): Drawable {
    return mutate().apply {
        setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
}
