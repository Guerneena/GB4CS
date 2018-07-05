package com.hklouch.ui

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder

abstract class ResourceBaseAdapter<T, U : ViewHolder> : RecyclerView.Adapter<U>() {

    var itemListener: ItemListener<T>? = null

    abstract fun containsElements(): Boolean
    abstract fun bindItems(items: List<T>)
    abstract fun clearData()

    interface ItemListener<T> {
        fun onItemClicked(item: T)
    }
}