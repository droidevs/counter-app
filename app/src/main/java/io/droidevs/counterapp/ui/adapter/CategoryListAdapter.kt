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
import io.droidevs.counterapp.databinding.ItemCategoryBinding
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.ui.listeners.OnCategoryClickListener
import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.utils.CategoryColorUtil

class CategoryListAdapter(
    val listener: OnCategoryClickListener? = null
) :
    ListAdapter<CategoryUiModel, CategoryListAdapter.CategoryVH>(Diff()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return CategoryVH(binding)
    }


    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            listener?.onCategoryClick(getItem(position))
        }
    }


    inner class CategoryVH(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        private val name = binding.tvCategoryName
        private val countersCount = binding.tvCountersCount

        fun bind(category : CategoryUiModel) {
            name.text = category.name
            countersCount.text = "${category.countersCount} counters"

            val drawable =
                ContextCompat.getDrawable(itemView.context, R.drawable.bg_category_card)
                    ?.mutate()
            var color : Int
            if (category.color.colorInt != 0){
                color = category.color.colorInt
            } else {
                color = CategoryColorUtil.generateColor(
                    context = itemView.context,
                    category = category.toDomain()
                )
            }
            drawable?.setTint(color)
            // todo : alpha based on the number of counters
            binding.container.background = drawable


        }
    }

    class Diff : DiffUtil.ItemCallback<CategoryUiModel>() {
        override fun areItemsTheSame(old: CategoryUiModel, new: CategoryUiModel) =
            old.id == new.id

        override fun areContentsTheSame(old: CategoryUiModel, new: CategoryUiModel) =
            old == new
    }
}