package io.droidevs.counterapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.ItemEmptyAddBinding
import io.droidevs.counterapp.databinding.ItemHomeCounterBinding
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.ui.adapter.base.DiffUpdate
import io.droidevs.counterapp.ui.adapter.models.CounterWithCategoryItem
import io.droidevs.counterapp.domain.display.DisplayPreferences
import io.droidevs.counterapp.ui.listeners.OnCounterClickListener
import io.droidevs.counterapp.ui.models.CounterWithCategoryUiModel
import io.droidevs.counterapp.ui.utils.CategoryColorUtil
import io.droidevs.counterapp.ui.utils.NoCategoryUi

internal class HomeCounterAdapter(
    counters: MutableList<CounterWithCategoryUiModel>,
    private val listener: OnCounterClickListener? = null,
    private val onAddCounter: () -> Unit = {},
    private val onIncrement: (counter: CounterWithCategoryUiModel) -> Unit = {},
    private val onDecrement: (counter: CounterWithCategoryUiModel) -> Unit = {},
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diff = DiffUpdate.diffable<CounterWithCategoryItem>()

    private val items: MutableList<CounterWithCategoryItem> = counters.map { CounterWithCategoryItem(it) }.toMutableList()

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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_TYPE_ADD -> {
                val binding = ItemEmptyAddBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AddViewHolder(binding)
            }

            else -> {
                val binding = ItemHomeCounterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val item = items[position]
            val cwg = item.model

            holder.bind(
                cwg = cwg,
                onIncrement = onIncrement,
                onDecrement = onDecrement,
                showControls = !displayPreferences.hideControls,
                showCategoryLabel = !displayPreferences.hideCounterCategoryLabel,
                showLastUpdate = !displayPreferences.hideLastUpdate,
            )

            holder.itemView.setOnClickListener {
                listener?.onCounterClick(cwg.counter)
            }
        } else {
            (holder as AddViewHolder).bind(onClick = onAddCounter)
        }
    }

    override fun getItemCount(): Int = items.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size) VIEW_TYPE_ADD else VIEW_TYPE_COUNTER
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
            onDecrement: (CounterWithCategoryUiModel) -> Unit,
            showControls: Boolean,
            showCategoryLabel: Boolean,
            showLastUpdate: Boolean,
        ) {
            name.text = cwg.counter.name
            count.text = cwg.counter.currentCount.toString()

            category.isVisible = showCategoryLabel

            btnPlus.isVisible = showControls
            btnMinus.isVisible = showControls

            btnPlus.isEnabled = cwg.counter.canIncrease
            btnMinus.isEnabled = cwg.counter.canDecrease

            if (showControls) {
                btnPlus.setOnClickListener { onIncrement(cwg) }
                btnMinus.setOnClickListener { onDecrement(cwg) }
            } else {
                btnPlus.setOnClickListener(null)
                btnMinus.setOnClickListener(null)
            }

            tvEditTime.isVisible = showLastUpdate && !cwg.counter.editedTime.isNullOrBlank()
            if (tvEditTime.isVisible) {
                tvEditTime.text = itemView.context.getString(R.string.edited_time_ago, cwg.counter.editedTime)
            }

            val drawable = ContextCompat
                .getDrawable(itemView.context, R.drawable.bg_chip)
                ?.mutate()

            val categoryUi = cwg.category
            if (categoryUi != null) {
                category.text = categoryUi.name

                val color = if (categoryUi.color.colorInt != 0) {
                    categoryUi.color.colorInt
                } else {
                    CategoryColorUtil.generateColor(
                        context = itemView.context,
                        category = categoryUi.toDomain()
                    )
                }
                drawable?.setTint(color)
                category.background = drawable
            } else {
                // No category
                category.setText(NoCategoryUi.labelRes())
                drawable?.setTint(NoCategoryUi.chipColor(itemView.context))
                category.background = drawable
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

    fun updateCounters(counters: List<CounterWithCategoryUiModel>) {
        val old = items.toList()
        val newItems = counters.map { CounterWithCategoryItem(it) }

        items.clear()
        items.addAll(newItems)

        diff.apply(adapter = this, old = old, new = newItems)

        // Ensure trailing ADD cell is up-to-date.
        notifyItemChanged(itemCount - 1)
    }

    companion object {
        private const val VIEW_TYPE_COUNTER = 0
        private const val VIEW_TYPE_ADD = 1
    }
}
