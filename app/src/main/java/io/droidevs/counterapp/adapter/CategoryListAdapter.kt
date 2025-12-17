package io.droidevs.counterapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.databinding.ItemCategoryBinding
import io.droidevs.counterapp.ui.CounterCategoryUiModel

class CategoryListAdapter() :
    ListAdapter<CounterCategoryUiModel, CategoryListAdapter.CategoryVH>(Diff()) {
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
    }


    inner class CategoryVH(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        private val name = binding.tvCategoryName
        private val countersCount = binding.tvCountersCount

        fun bind(item : CounterCategoryUiModel) {
            name.text = item.name
            countersCount.text = "${item.countersCount} counters"

        }
    }

    class Diff : DiffUtil.ItemCallback<CounterCategoryUiModel>() {
        override fun areItemsTheSame(old: CounterCategoryUiModel, new: CounterCategoryUiModel) =
            old.id == new.id

        override fun areContentsTheSame(old: CounterCategoryUiModel, new: CounterCategoryUiModel) =
            old == new
    }
}