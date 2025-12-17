package io.droidevs.counterapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.databinding.ItemCategoryCounterBinding
import io.droidevs.counterapp.ui.models.CounterSnapshot

class CategoryCountersAdapter :
    ListAdapter<CounterSnapshot, CategoryCountersAdapter.ViewHolder>(Diff()) {

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
        }
    }

    class Diff : DiffUtil.ItemCallback<CounterSnapshot>() {
        override fun areItemsTheSame(old: CounterSnapshot, new: CounterSnapshot) =
            old.id == new.id

        override fun areContentsTheSame(old: CounterSnapshot, new: CounterSnapshot) =
            old == new
    }
}
