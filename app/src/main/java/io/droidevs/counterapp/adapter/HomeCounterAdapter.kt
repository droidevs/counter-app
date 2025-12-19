package io.droidevs.counterapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.databinding.ItemEmptyAddBinding
import io.droidevs.counterapp.databinding.ItemHomeCounterBinding
import io.droidevs.counterapp.ui.listeners.OnCounterClickListener
import io.droidevs.counterapp.ui.models.CounterWithCategoryUiModel


internal class HomeCounterAdapter(
    private val counters: MutableList<CounterWithCategoryUiModel>,
    private val listener: OnCounterClickListener? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_TYPE_ADD -> {
                var binding = ItemEmptyAddBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AddViewHolder(binding)
            }

            else -> {
                var binding = ItemHomeCounterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(@NonNull holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            var cwg = counters[position]
            holder.bind(cwg)
            holder.itemView.setOnClickListener {
                listener?.onCounterClick(cwg.counter)
            }
        } else {
            (holder as AddViewHolder).bind()
        }
    }

    override fun getItemCount(): Int = counters.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == counters.size) VIEW_TYPE_ADD else VIEW_TYPE_COUNTER
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

    class AddViewHolder(binding: ItemEmptyAddBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            itemView.setOnClickListener {
                // todo : fire action click
                itemView.animate()
                    .scaleX(1.05f)
                    .scaleY(1.05f)
                    .setDuration(600)
                    .withEndAction {
                        itemView.animate().scaleX(1f).scaleY(1f).duration = 600
                    }
                    .start()
            }
        }
    }


    public fun updateCounters(counters: List<CounterWithCategoryUiModel>) {
        this.counters.clear()
        this.counters.addAll(counters)
        notifyDataSetChanged()
    }

    companion object {
        private const val VIEW_TYPE_COUNTER = 0
        private const val VIEW_TYPE_ADD = 1
    }
}
