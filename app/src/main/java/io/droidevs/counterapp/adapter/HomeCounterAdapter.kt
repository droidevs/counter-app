package io.droidevs.counterapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.databinding.ItemHomeCounterBinding
import io.droidevs.counterapp.ui.CounterSnapshot
import io.droidevs.counterapp.ui.OnCounterClickListener


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
        var name: TextView = binding.tvCounterName
        var count = binding.tvCurrentCount
        var createdAt = binding.tvCreatedDate
        var updatedAt = binding.tvLastEditedDate

        fun bind(counter: CounterSnapshot) {
            name.text = counter.name
            count.text = counter.currentCount.toString()
            createdAt.text = counter.createdAt.toString()
            updatedAt.text = counter.lastUpdatedAt.toString()
        }
    }

    public fun updateCounters(counters: List<CounterSnapshot>) {
        this.counters.clear()
        this.counters.addAll(counters)
        notifyDataSetChanged()
    }
}
