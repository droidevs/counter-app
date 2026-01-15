package io.droidevs.counterapp.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.ItemEmptyAddBinding
import io.droidevs.counterapp.databinding.ItemHomeCounterBinding
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.ui.listeners.OnCounterClickListener
import io.droidevs.counterapp.ui.models.CounterWithCategoryUiModel
import io.droidevs.counterapp.ui.utils.CategoryColorUtil
import io.droidevs.counterapp.ui.utils.CategoryColorUtil.isDark
import io.droidevs.counterapp.ui.utils.getRelativeTime


internal class HomeCounterAdapter(
    private val counters: MutableList<CounterWithCategoryUiModel>,
    private val listener: OnCounterClickListener? = null,
    private val onAddCounter : () -> Unit = {},
    private val onIncrement : (counter: CounterWithCategoryUiModel) -> Unit = {},
    private val onDecrement : (counter: CounterWithCategoryUiModel) -> Unit = {}
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
            holder.bind(
                cwg = cwg,
                onIncrement = onIncrement,
                onDecrement = onDecrement
            )
            holder.itemView.setOnClickListener {
                listener?.onCounterClick(cwg.counter)
            }

        } else {
            (holder as AddViewHolder).bind(
                onClick = onAddCounter
            )
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
        var tvEditTime = binding.tvEditedTime
        var btnPlus = binding.btnPlus
        var btnMinus = binding.btnMinus

        fun bind(
            cwg: CounterWithCategoryUiModel,
            onIncrement: (CounterWithCategoryUiModel) -> Unit,
            onDecrement: (CounterWithCategoryUiModel) -> Unit
        ) {
            name.text = cwg.counter.name
            count.text = cwg.counter.currentCount.toString()
            category.text = cwg.category?.name

            btnPlus.setOnClickListener {
                onIncrement(cwg)
            }
            btnMinus.setOnClickListener {
                onDecrement(cwg)
            }

            cwg.category?.let {
                val drawable = ContextCompat
                    .getDrawable(itemView.context, R.drawable.bg_chip)
                    ?.mutate()
                var color : Int
                if (it.color.colorInt != 0) {
                    color = it.color.colorInt
                } else {
                    color = CategoryColorUtil.generateColor(
                        context = itemView.context,
                        category = it.toDomain()
                    )
                }
                drawable?.setTint(color)
                category.background = drawable

                count.text = itemView.context.resources.getQuantityString(
                    R.plurals.counters_count, it.countersCount, it.countersCount
                )

                if (!it.editedTime.isNullOrBlank()) {
                    tvEditTime.text = itemView.context.getString(
                        R.string.edited_time_ago,
                        it.editedTime
                    )
                    tvEditTime.isVisible = true
                } else {
                    tvEditTime.isVisible = false
                }

            }
        }
    }

    class AddViewHolder(binding: ItemEmptyAddBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            onClick: () -> Unit = {}
        ) {
            itemView.setOnClickListener {
                itemView.animate()
                    .scaleX(1.05f)
                    .scaleY(1.05f)
                    .setDuration(400)
                    .withEndAction {
                        itemView.animate().scaleX(1f).scaleY(1f).duration = 600
                    }
                    .start()
                onClick()
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
