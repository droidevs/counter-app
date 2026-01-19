package io.droidevs.counterapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.ItemListCounterBinding
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.ui.adapter.base.DiffUpdate
import io.droidevs.counterapp.ui.adapter.models.CounterWithCategoryItem
import io.droidevs.counterapp.domain.display.DisplayPreferences
import io.droidevs.counterapp.ui.listeners.OnCounterClickListener
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.models.CounterWithCategoryUiModel
import io.droidevs.counterapp.ui.system.SystemCounterSupportStatus
import io.droidevs.counterapp.ui.system.SystemCounterSupportUi
import io.droidevs.counterapp.ui.utils.CategoryColorUtil
import io.droidevs.counterapp.ui.utils.NoCategoryUi

class ListCounterAdapter(
    // Public API remains UI models. Internal storage uses DiffableItem wrappers.
    counters: List<CounterWithCategoryUiModel> = emptyList(),
    private val listener: OnCounterClickListener,
    private val onIncrement: (counter: CounterUiModel) -> Unit,
    private val onDecrement: (counter: CounterUiModel) -> Unit,
) : RecyclerView.Adapter<ListCounterAdapter.ViewHolder>() {

    private val diff = DiffUpdate.diffable<CounterWithCategoryItem>()

    private var items: List<CounterWithCategoryItem> = counters.map { CounterWithCategoryItem(it) }

    private var displayPreferences: DisplayPreferences = DisplayPreferences(
        hideControls = false,
        hideLastUpdate = false,
        hideCounterCategoryLabel = false,
    )

    fun updateDisplayPreferences(prefs: DisplayPreferences) {
        if (displayPreferences == prefs) return
        displayPreferences = prefs
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        val binding: ItemListCounterBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val tvName: TextView = binding.tvCounterName
        val tvCount: TextView = binding.tvCounterValue
        val tvEditTime: TextView = binding.tvEditedTime
        val tvCategory: TextView = binding.tvCategory
        val tvSystemHint: TextView = binding.tvSystemHint

        fun bind(
            item: CounterWithCategoryItem,
            onIncrement: (counter: CounterUiModel) -> Unit,
            onDecrement: (counter: CounterUiModel) -> Unit
        ) {
            val data = item.model

            tvName.text = data.counter.name
            tvCount.text = data.counter.currentCount.toString()

            tvCategory.isVisible = !displayPreferences.hideCounterCategoryLabel

            val showEdited = !displayPreferences.hideLastUpdate
            // Edited timestamp belongs to the counter, not the category.
            if (showEdited && !data.counter.editedTime.isNullOrBlank()) {
                tvEditTime.text = itemView.context.getString(
                    R.string.edited_time_ago,
                    data.counter.editedTime
                )
                tvEditTime.isVisible = true
            } else {
                tvEditTime.isVisible = false
            }

            if (data.category != null) {
                val categoryUi = data.category
                tvCategory.text = categoryUi.name

                val color = if (categoryUi.color.colorInt != 0) {
                    categoryUi.color.colorInt
                } else {
                    CategoryColorUtil.generateColor(
                        context = itemView.context,
                        category = categoryUi.toDomain()
                    )
                }
                val drawable = ContextCompat
                    .getDrawable(itemView.context, R.drawable.bg_chip)
                    ?.mutate()
                drawable?.setTint(color)
                tvCategory.background = drawable
            } else {
                tvCategory.setText(NoCategoryUi.labelRes())
                val drawable = ContextCompat
                    .getDrawable(itemView.context, R.drawable.bg_chip)
                    ?.mutate()
                drawable?.setTint(NoCategoryUi.chipColor(itemView.context))
                tvCategory.background = drawable
            }

            // If labels are disabled, always hide the category chip.
            if (displayPreferences.hideCounterCategoryLabel) {
                tvCategory.isVisible = false
            }

            binding.root.setOnClickListener {
                listener.onCounterClick(data.counter)
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

            val showControls = !displayPreferences.hideControls
            binding.btnPlus.isVisible = showControls && !data.counter.isSystem
            binding.btnMinus.isVisible = showControls && !data.counter.isSystem
            if (showControls && !data.counter.isSystem) {
                binding.btnPlus.setOnClickListener { onIncrement(data.counter) }
                binding.btnMinus.setOnClickListener { onDecrement(data.counter) }
            } else {
                binding.btnPlus.setOnClickListener(null)
                binding.btnMinus.setOnClickListener(null)
            }

            binding.btnPlus.isEnabled = allowControls
            binding.btnMinus.isEnabled = allowControls
            binding.btnPlus.alpha = if (allowControls) 1f else 0.5f
            binding.btnMinus.alpha = if (allowControls) 1f else 0.5f
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemListCounterBinding = ItemListCounterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(
            item = item,
            onIncrement = onIncrement,
            onDecrement = onDecrement
        )
    }

    override fun getItemCount(): Int = items.size

    /** Used by scroll tracking logic (visible items). */
    fun getCounterAt(position: Int): CounterUiModel? = items.getOrNull(position)?.model?.counter

    fun updateCounters(counters: List<CounterWithCategoryUiModel>) {
        val old = items
        val newItems = counters.map { CounterWithCategoryItem(it) }

        items = newItems
        diff.apply(adapter = this, old = old, new = newItems)
    }
}
