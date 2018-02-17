package net.bogus.githubsearch.adapter

import android.support.v7.util.DiffUtil
import net.bogus.githubsearch.model.Repository


class RepositoryAdapterDiffCallback : DiffUtil.Callback {

    var oldItems:List<Repository>? = null
    var newItems:List<Repository>? = null

    constructor(oldItems: List<Repository>?, newItems: List<Repository>?) : super() {
        this.oldItems = oldItems
        this.newItems = newItems
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems?.get(oldItemPosition)?.equals(newItems?.get(newItemPosition)) ?: false
    }

    override fun getOldListSize(): Int {
        return oldItems?.count() ?: 0
    }

    override fun getNewListSize(): Int {
        return newItems?.count() ?: 0
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems?.get(oldItemPosition)?.equals(newItems?.get(newItemPosition)) ?: false
    }

}