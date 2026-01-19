package io.droidevs.counterapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.databinding.ItemHistoryBinding
import io.droidevs.counterapp.ui.adapter.base.DiffListAdapter
import io.droidevs.counterapp.ui.adapter.models.HistoryItem
import io.droidevs.counterapp.ui.models.HistoryUiModel

class HistoryAdapter : DiffListAdapter<HistoryItem, HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position).model)
    }

    fun submitUiModels(list: List<HistoryUiModel>) {
        submitList(list.map { HistoryItem(it) })
    }

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryUiModel) {
            binding.textViewCounterName.text = history.counterName
            binding.textViewChange.text = history.change.toString()
            binding.textViewTimestamp.text = history.createdTime
        }
    }
}
