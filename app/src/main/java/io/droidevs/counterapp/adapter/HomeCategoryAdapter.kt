package io.droidevs.counterapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.databinding.ItemHomeCategoryBinding
import io.droidevs.counterapp.ui.listeners.OnCategoryClickListener
import io.droidevs.counterapp.ui.models.CategoryUiModel

class HomeCategoryAdapter(
    private val listener: OnCategoryClickListener? = null
) : ListAdapter<CategoryUiModel, HomeCategoryAdapter.CategoryViewHolder>(
    DiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHomeCategoryBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            listener?.onCategoryClick(getItem(position))
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

    override fun onViewAttachedToWindow(holder: CategoryViewHolder) {
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
}
