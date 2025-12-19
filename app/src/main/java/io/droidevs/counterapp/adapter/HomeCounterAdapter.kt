package io.droidevs.counterapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.databinding.ItemHomeCounterBinding
import io.droidevs.counterapp.ui.models.CounterSnapshot
import io.droidevs.counterapp.ui.listeners.OnCounterClickListener


internal class HomeCounterAdapter(
    private val counters: MutableList<CounterSnapshot>,
    private val listener: OnCounterClickListener? = null
) :
    RecyclerView.Adapter<HomeCounterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): ViewHolder {
        var binding = ItemHomeCounterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        var counter = counters[position]
        holder.bind(counter)
        holder.itemView.setOnClickListener {
            listener?.onCounterClick(counter)
        }
    }

    override fun getItemCount(): Int {
        return counters.size
    }

    internal class ViewHolder(binding: ItemHomeCounterBinding) : RecyclerView.ViewHolder(binding.root) {
        var name: TextView = binding.txtCounterName
        var category: TextView = binding.txtCategory
        var count = binding.txtCount
        var updatedAt = binding.txtUpdated

        fun bind(counter: CounterSnapshot) {
            name.text = counter.name
            count.text = counter.currentCount.toString()
            category.text = "Category" // TODO: get category name from db
            updatedAt.text = counter.lastUpdatedAt.toString()
        }
    }

    public fun updateCounters(counters: List<CounterSnapshot>) {
        this.counters.clear()
        this.counters.addAll(counters)
        notifyDataSetChanged()
    }
}
