package io.droidevs.counterapp.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.ItemEmptyAddBinding
import io.droidevs.counterapp.databinding.ItemHomeCategoryBinding
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.ui.listeners.OnCategoryClickListener
import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.utils.CategoryColorUtil
import io.droidevs.counterapp.ui.utils.CategoryColorUtil.isDark

class HomeCategoryAdapter(
    private val listener: OnCategoryClickListener? = null,
    private val onAdd: (() -> Unit)? = null
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
            (holder as AddViewHolder).bind(onAdd ?: {})
        }
    }

    inner class CategoryViewHolder(binding: ItemHomeCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        private val name = binding.categoryName
        private val count = binding.categoryCount
        private val container = binding.container

        fun bind(category: CategoryUiModel) {
            name.text = category.name
            count.text = category.countersCount.toString()

            val drawable = ContextCompat
                .getDrawable(itemView.context, R.drawable.bg_category_card)
                ?.mutate()


            if (category.color.colorInt != 0) {
                drawable?.setTint(category.color.colorInt)
            } else {
                val color = CategoryColorUtil.generateColor(context = itemView.context, category = category.toDomain())
                drawable?.setTint(color)
            }
            container.background = drawable

        }
    }

    class AddViewHolder(binding: ItemEmptyAddBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            onClick: () -> Unit = {}
        ) {
            itemView.setOnClickListener {
                itemView.animate()
                    .scaleX(1.05f)
                    .scaleY(1.05f)
                    .setDuration(400)
                    .withEndAction {
                        itemView.animate().scaleX(1f).scaleY(1f).duration = 600
                    }
                    .start()
                onClick()
            }
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
