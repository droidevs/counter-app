package io.droidevs.counterapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.ItemCategoryCounterBinding
import io.droidevs.counterapp.ui.models.CounterUiModel

class CategoryCountersAdapter :
    ListAdapter<CounterUiModel, CategoryCountersAdapter.ViewHolder>(Diff()) {

    inner class ViewHolder(val binding: ItemCategoryCounterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryCounterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            tvCounterName.text = item.name
            tvCounterValue.text = item.currentCount.toString()
            if (!item.editedTime.isNullOrBlank()) {
                tvEditedTime.text = holder.itemView.context.getString(
                    R.string.edited_time_ago,
                    item.editedTime
                )
                tvEditedTime.isVisible = true
            } else {
                tvEditedTime.isVisible = false
            }
        }
    }

    class Diff : DiffUtil.ItemCallback<CounterUiModel>() {
        override fun areItemsTheSame(old: CounterUiModel, new: CounterUiModel) =
            old.id == new.id

        override fun areContentsTheSame(old: CounterUiModel, new: CounterUiModel) =
            old == new
    }
}
