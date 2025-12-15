package io.droidevs.counterapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.ItemListCounterBinding

class ListCounterAdapter(
    private val counters: List<String> // dummy data for now
) : RecyclerView.Adapter<ListCounterAdapter.ViewHolder>() {

    inner class ViewHolder(
        binding: ItemListCounterBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val tvName: TextView = binding.tvCounterName
        val tvCount: TextView = binding.tvCounterCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding : ItemListCounterBinding = ItemListCounterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = counters[position]
        // todo : use real data
        holder.tvCount.text = (0..10).random().toString() // random dummy count
    }

    override fun getItemCount(): Int = counters.size
}
