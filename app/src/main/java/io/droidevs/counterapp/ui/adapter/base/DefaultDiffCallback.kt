package io.droidevs.counterapp.ui.adapter.base

import androidx.recyclerview.widget.DiffUtil
import java.util.Objects

class DefaultDiffCallback<T : DiffableItem> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.diffId == newItem.diffId

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        Objects.equals(oldItem.diffContent,newItem.diffContent)
}

