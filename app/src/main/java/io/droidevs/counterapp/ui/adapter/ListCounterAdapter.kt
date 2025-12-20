package io.droidevs.counterapp.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.ItemListCounterBinding
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.ui.listeners.OnCounterClickListener
import io.droidevs.counterapp.ui.models.CounterWithCategoryUiModel
import io.droidevs.counterapp.ui.utils.CategoryColorUtil
import io.droidevs.counterapp.ui.utils.CategoryColorUtil.isDark

class ListCounterAdapter(
    private var counters: List<CounterWithCategoryUiModel> = ArrayList<CounterWithCategoryUiModel>(),
    private val listener : OnCounterClickListener
) : RecyclerView.Adapter<ListCounterAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemListCounterBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val tvName: TextView = binding.tvCounterName
        val tvCount: TextView = binding.tvCounterValue
        val updatedAt : TextView = binding.tvUpdated
        val tvCategory : TextView = binding.tvCategory

        fun bind(data: CounterWithCategoryUiModel) {
            tvName.text = data.counter.name
            tvCount.text = data.counter.currentCount.toString()
            tvCategory.text = data.category?.name
            updatedAt.text = data.counter.lastUpdatedAt.toString()
            binding.root.setOnClickListener {
                listener.onCounterClick(data.counter)
            }

            data.category?.let {
                val color = CategoryColorUtil.generateColor(context = itemView.context, category = it.toDomain())

                val drawable = ContextCompat
                    .getDrawable(itemView.context, R.drawable.bg_chip)
                    ?.mutate()

                drawable?.setTint(color)
                tvCategory.background = drawable

                tvCategory.setTextColor(
                    if (isDark(color)) Color.WHITE else Color.BLACK
                )
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
    fun updateCounters(counters: List<CounterWithCategoryUiModel>) {
        var l = this.counters as ArrayList<CounterWithCategoryUiModel>
        l.clear()
        l.addAll(counters)
        notifyDataSetChanged()
    }
}
