package io.droidevs.counterapp.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.ItemListCounterBinding
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.ui.listeners.OnCounterClickListener
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.models.CounterWithCategoryUiModel
import io.droidevs.counterapp.ui.utils.CategoryColorUtil
import io.droidevs.counterapp.ui.utils.CategoryColorUtil.isDark
import io.droidevs.counterapp.ui.utils.getRelativeTime
import io.droidevs.counterapp.ui.system.SystemCounterSupportStatus
import io.droidevs.counterapp.ui.system.SystemCounterSupportUi

class ListCounterAdapter(
    var counters: List<CounterWithCategoryUiModel> = ArrayList<CounterWithCategoryUiModel>(),
    private val listener : OnCounterClickListener,
    private val onIncrement : (counter: CounterUiModel) -> Unit,
    private val onDecrement : (counter: CounterUiModel) -> Unit,
) : RecyclerView.Adapter<ListCounterAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemListCounterBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val tvName: TextView = binding.tvCounterName
        val tvCount: TextView = binding.tvCounterValue
        val tvEditTime : TextView = binding.tvEditedTime
        val tvCategory : TextView = binding.tvCategory
        val tvSystemHint: TextView = binding.tvSystemHint

        fun bind(
            data: CounterWithCategoryUiModel,
            onIncrement: (counter: CounterUiModel) -> Unit,
            onDecrement: (counter: CounterUiModel) -> Unit
        ) {
            tvName.text = data.counter.name
            tvCount.text = data.counter.currentCount.toString()
            tvCategory.text = data.category?.name
            tvEditTime.text = data.counter.editedTime

            binding.root.setOnClickListener {
                listener.onCounterClick(data.counter)
            }
            binding.btnPlus.setOnClickListener {
                onIncrement(data.counter)
            }

            binding.btnMinus.setOnClickListener {
                onDecrement(data.counter)
            }

            data.category?.let {
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
                tvCategory.background = drawable

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

            val ctx = itemView.context

            val hint = if (data.counter.isSystem) SystemCounterSupportUi.hintText(ctx, data.counter.systemKey) else null

            if (hint != null) {
                tvSystemHint.text = hint
                tvSystemHint.visibility = android.view.View.VISIBLE
                tvSystemHint.animate().cancel()
                tvSystemHint.alpha = 0f
                tvSystemHint.animate().alpha(1f).setDuration(220L).start()

                tvSystemHint.setOnClickListener {
                    val status = SystemCounterSupportStatus.evaluate(ctx, data.counter.systemKey)
                    MaterialAlertDialogBuilder(ctx)
                        .setTitle(status.titleRes)
                        .setMessage(status.messageRes)
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
                }
            } else {
                tvSystemHint.animate().cancel()
                tvSystemHint.visibility = android.view.View.GONE
            }

            // Disable +/- if a known system counter is not supported (prevents misleading UI interactions).
            val allowControls = if (data.counter.isSystem && hint != null) {
                SystemCounterSupportStatus.evaluate(ctx, data.counter.systemKey).isSupported
            } else true

            binding.btnPlus.isEnabled = allowControls
            binding.btnMinus.isEnabled = allowControls
            binding.btnPlus.alpha = if (allowControls) 1f else 0.5f
            binding.btnMinus.alpha = if (allowControls) 1f else 0.5f
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding: ItemListCounterBinding = ItemListCounterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val counter = counters[position]
        holder.bind(
            data = counter,
            onIncrement = onIncrement,
            onDecrement = onDecrement
        )
    }

    override fun getItemCount(): Int = counters.size
    fun updateCounters(counters: List<CounterWithCategoryUiModel>) {
        var l = this.counters as ArrayList<CounterWithCategoryUiModel>
        l.clear()
        l.addAll(counters)
        notifyDataSetChanged()
    }
}
