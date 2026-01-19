package io.droidevs.counterapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.ItemCategoryBinding
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.ui.adapter.base.DiffListAdapter
import io.droidevs.counterapp.ui.adapter.models.CategoryItem
import io.droidevs.counterapp.ui.listeners.OnCategoryClickListener
import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.utils.CategoryColorUtil

class CategoryListAdapter(
    val listener: OnCategoryClickListener? = null
) : DiffListAdapter<CategoryItem, CategoryListAdapter.CategoryVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return CategoryVH(binding)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        val item = getItem(position).model
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener?.onCategoryClick(item)
        }
    }

    fun submitUiModels(categories: List<CategoryUiModel>) {
        submitList(categories.map { CategoryItem(it) })
    }

    inner class CategoryVH(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        private val name = binding.tvCategoryName
        private val count = binding.tvCountersCount

        fun bind(category: CategoryUiModel) {
            name.text = category.name
            count.text = itemView.context.resources.getQuantityString(
                R.plurals.counters_count, category.countersCount, category.countersCount
            )

            val drawable =
                ContextCompat.getDrawable(itemView.context, R.drawable.bg_category_card)
                    ?.mutate()

            val color: Int = if (category.color.colorInt != 0) {
                category.color.colorInt
            } else {
                CategoryColorUtil.generateColor(
                    context = itemView.context,
                    category = category.toDomain()
                )
            }

            drawable?.setTint(color)
            binding.container.background = drawable

            if (category.editedTime != null) {
                binding.tvEditedTime.text = itemView.context.getString(
                    R.string.edited_time_ago,
                    category.editedTime
                )
                binding.tvEditedTime.isVisible = true
            } else {
                binding.tvEditedTime.isVisible = false
            }
        }
    }
}