package io.droidevs.counterapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.databinding.ItemHomeCounterBinding
import io.droidevs.counterapp.ui.models.CounterSnapshot
import io.droidevs.counterapp.ui.listeners.OnCounterClickListener
import io.droidevs.counterapp.ui.models.CounterWithCategoryUiModel


internal class HomeCounterAdapter(
    private val counters: MutableList<CounterWithCategoryUiModel>,
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
        var cwg = counters[position]
        holder.bind(cwg)
        holder.itemView.setOnClickListener {
            listener?.onCounterClick(cwg.counter)
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
        var btnPlus = binding.btnPlus
        var btnMinus = binding.btnMinus

        fun bind(cwg: CounterWithCategoryUiModel) {
            name.text = cwg.counter.name
            count.text = cwg.counter.currentCount.toString()
            category.text = cwg.category?.name
            updatedAt.text = cwg.counter.lastUpdatedAt.toString()
        }
    }

    public fun updateCounters(counters: List<CounterWithCategoryUiModel>) {
        this.counters.clear()
        this.counters.addAll(counters)
        notifyDataSetChanged()
    }
}
