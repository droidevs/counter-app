package io.droidevs.counterapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.adapter.HomeCounterAdapter.AddViewHolder
import io.droidevs.counterapp.databinding.ItemEmptyAddBinding
import io.droidevs.counterapp.databinding.ItemHomeCategoryBinding
import io.droidevs.counterapp.ui.listeners.OnCategoryClickListener
import io.droidevs.counterapp.ui.models.CategoryUiModel

class HomeCategoryAdapter(
    private val listener: OnCategoryClickListener? = null
) : ListAdapter<CategoryUiModel, RecyclerView.ViewHolder>(
    DiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_ADD -> {
                var binding = ItemEmptyAddBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                    )
                AddViewHolder(binding)
            }
            else -> {
                var binding = ItemHomeCategoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                    )
                CategoryViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryViewHolder) {
            holder.bind(getItem(position))
            holder.itemView.setOnClickListener {
                listener?.onCategoryClick(getItem(position))
            }
        }
        else {
            (holder as AddViewHolder).bind()
        }
    }

    inner class CategoryViewHolder(binding: ItemHomeCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        private val name = binding.categoryName
        private val count = binding.categoryCount

        fun bind(item: CategoryUiModel) {
            name.text = item.name
            count.text = item.countersCount.toString()
        }
    }

    override fun getItemCount(): Int = currentList.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == currentList.size) VIEW_TYPE_ADD else VIEW_TYPE_CATEGORY
    }
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
    }

    private object DiffCallback : DiffUtil.ItemCallback<CategoryUiModel>() {
        override fun areItemsTheSame(
            oldItem: CategoryUiModel,
            newItem: CategoryUiModel
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: CategoryUiModel,
            newItem: CategoryUiModel
        ): Boolean = oldItem == newItem
    }

    companion object {
        private const val VIEW_TYPE_CATEGORY = 0
        private const val VIEW_TYPE_ADD = 1
    }
}
