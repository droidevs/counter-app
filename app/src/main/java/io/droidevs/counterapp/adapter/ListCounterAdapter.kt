package io.droidevs.counterapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.ItemListCounterBinding
import io.droidevs.counterapp.ui.CounterSnapshot
import io.droidevs.counterapp.ui.OnCounterClickListener

class ListCounterAdapter(
    private var counters: List<CounterSnapshot> = ArrayList<CounterSnapshot>(),
    private val listener : OnCounterClickListener
) : RecyclerView.Adapter<ListCounterAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemListCounterBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val tvName: TextView = binding.tvCounterName
        val tvCount: TextView = binding.tvCounterCount

        fun bind(counter: CounterSnapshot) {
            tvName.text = counter.name
            tvCount.text = counter.currentCount.toString()
            binding.root.setOnClickListener {
                listener.onCounterClick(counter)
            }
        }
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
        val counter = counters[position]
        holder.bind(counter)
    }

    override fun getItemCount(): Int = counters.size
    fun updateCounters(counters: List<CounterSnapshot>) {
        var l = this.counters as ArrayList<CounterSnapshot>
        l.clear()
        l.addAll(counters)
        notifyDataSetChanged()
    }
}
