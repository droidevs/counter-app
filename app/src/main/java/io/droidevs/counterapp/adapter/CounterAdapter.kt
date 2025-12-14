package io.droidevs.counterapp.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.databinding.ItemCounterBinding
import io.droidevs.counterapp.model.CounterSnapshot


internal class CounterAdapter(private val counters: MutableList<CounterSnapshot>) :
    RecyclerView.Adapter<CounterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): ViewHolder {
        var binding = ItemCounterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        var counter = counters[position]
        holder.name.text = counter.name
        holder.count.text = counter.currentCount.toString()
        holder.maxCount.text = counter.maxCount?.toString()
        holder.createdAt.text = counter.createdAt.toString()
        holder.updatedAt.text = counter.lastUpdatedAt.toString()
    }

    override fun getItemCount(): Int {
        return counters.size
    }

    internal class ViewHolder(binding: ItemCounterBinding) : RecyclerView.ViewHolder(binding.root) {
        var name: TextView = binding.tvCounterName
        var count = binding.tvCurrentCount
        var maxCount = binding.tvMaxCount
        var createdAt = binding.tvCreatedDate
        var updatedAt = binding.tvLastEditedDate
    }
}
