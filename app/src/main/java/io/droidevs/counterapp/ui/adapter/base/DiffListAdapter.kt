package io.droidevs.counterapp.ui.adapter.base

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Base ListAdapter using a shared diff callback.
 *
 * Keeps diff logic centralized and consistent.
 */
abstract class DiffListAdapter<T : DiffableItem, VH : RecyclerView.ViewHolder> :
    ListAdapter<T, VH>(DefaultDiffCallback())
