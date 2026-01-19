package io.droidevs.counterapp.ui.adapter.base

import androidx.recyclerview.widget.RecyclerView

/**
 * Shared helper to update a RecyclerView.Adapter list with DiffUtil.
 *
 * Use this for adapters that aren't ListAdapter (multi-view-type, needs custom storage, etc.)
 * to avoid calling notifyDataSetChanged.
 */
class DiffUpdate<T>(
    private val id: (T) -> Any,
    private val content: (T) -> Any = { it as Any }
) {

    companion object {
        fun <T : DiffableItem> diffable(): DiffUpdate<T> =
            DiffUpdate(
                id = { it.diffId },
                content = { it.diffContent }
            )
    }

    fun calculate(old: List<T>, new: List<T>): androidx.recyclerview.widget.DiffUtil.DiffResult {
        return androidx.recyclerview.widget.DiffUtil.calculateDiff(object : androidx.recyclerview.widget.DiffUtil.Callback() {
            override fun getOldListSize(): Int = old.size
            override fun getNewListSize(): Int = new.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return id(old[oldItemPosition]) == id(new[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return content(old[oldItemPosition]) == content(new[newItemPosition])
            }
        })
    }

    fun apply(adapter: RecyclerView.Adapter<*>, old: List<T>, new: List<T>) {
        calculate(old, new).dispatchUpdatesTo(adapter)
    }
}
