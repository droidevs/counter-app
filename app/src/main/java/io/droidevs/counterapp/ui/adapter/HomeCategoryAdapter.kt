package io.droidevs.counterapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.ItemEmptyAddBinding
import io.droidevs.counterapp.databinding.ItemHomeCategoryBinding
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.ui.adapter.base.DiffUpdate
import io.droidevs.counterapp.ui.listeners.OnCategoryClickListener
import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.utils.CategoryColorUtil

class HomeCategoryAdapter(
    private val listener: OnCategoryClickListener? = null,
    private val onAdd: (() -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diff = DiffUpdate<CategoryUiModel>(
        id = { it.id },
        content = { it }
    )

    private var items: MutableList<CategoryUiModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ADD -> {
                val binding = ItemEmptyAddBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AddViewHolder(binding)
            }

            else -> {
                val binding = ItemHomeCategoryBinding.inflate(
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
            val item = items[position]
            holder.bind(item)
            holder.itemView.setOnClickListener { listener?.onCategoryClick(item) }
        } else {
            (holder as AddViewHolder).bind(onAdd ?: {})
        }
    }

    override fun getItemCount(): Int = items.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size) VIEW_TYPE_ADD else VIEW_TYPE_CATEGORY
    }

    fun submitUiModels(categories: List<CategoryUiModel>) {
        val old = items.toList()
        val newList = categories.toList()

        items = newList.toMutableList()
        diff.apply(
            adapter = this,
            old = old,
            new = newList
        )

        // Ensure the trailing ADD cell is accounted for.
        // DiffUpdate only covers the category items; the extra ADD row may need a minimal refresh.
        notifyItemChanged(itemCount - 1)
    }

    inner class CategoryViewHolder(binding: ItemHomeCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        private val name = binding.categoryName
        private val count = binding.categoryCount
        private val container = binding.container
        private val tvEditedTime = binding.tvEditedTime

        fun bind(category: CategoryUiModel) {
            name.text = category.name

            count.text = itemView.context.resources.getQuantityString(
                R.plurals.counters_count, category.countersCount, category.countersCount
            )

            val drawable = ContextCompat
                .getDrawable(itemView.context, R.drawable.bg_category_card)
                ?.mutate()

            val tint = if (category.color.colorInt != 0) {
                category.color.colorInt
            } else {
                CategoryColorUtil.generateColor(context = itemView.context, category = category.toDomain())
            }
            drawable?.setTint(tint)
            container.background = drawable

            if (!category.editedTime.isNullOrBlank()) {
                tvEditedTime.text = itemView.context.getString(
                    R.string.edited_time_ago,
                    category.editedTime
                )
                tvEditedTime.isVisible = true
            } else {
                tvEditedTime.isVisible = false
            }
        }
    }

    class AddViewHolder(binding: ItemEmptyAddBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(onClick: () -> Unit = {}) {
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

    companion object {
        private const val VIEW_TYPE_CATEGORY = 0
        private const val VIEW_TYPE_ADD = 1
    }
}
