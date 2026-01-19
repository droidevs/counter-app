package io.droidevs.counterapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.ItemCategoryCounterBinding
import io.droidevs.counterapp.ui.adapter.base.DiffListAdapter
import io.droidevs.counterapp.ui.adapter.models.CounterItem
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.system.SystemCounterSupportStatus
import io.droidevs.counterapp.ui.system.SystemCounterSupportUi

class CategoryCountersAdapter(
    private val onIncrement: (CounterUiModel) -> Unit = {},
    private val onDecrement: (CounterUiModel) -> Unit = {}
) : DiffListAdapter<CounterItem, CategoryCountersAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCategoryCounterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryCounterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position).model
        val ctx = holder.itemView.context

        with(holder.binding) {
            tvCounterName.text = item.name
            tvCounterValue.text = item.currentCount.toString()

            if (!item.editedTime.isNullOrBlank()) {
                tvEditedTime.text = ctx.getString(R.string.edited_time_ago, item.editedTime)
                tvEditedTime.isVisible = true
            } else {
                tvEditedTime.isVisible = false
            }

            // System hint chip
            val hint = if (item.isSystem) SystemCounterSupportUi.hintText(ctx, item.systemKey) else null
            if (hint != null) {
                tvSystemHint.text = hint
                tvSystemHint.visibility = View.VISIBLE
                tvSystemHint.animate().cancel()
                tvSystemHint.alpha = 0f
                tvSystemHint.animate().alpha(1f).setDuration(220L).start()

                tvSystemHint.setOnClickListener {
                    val status = SystemCounterSupportStatus.evaluate(ctx, item.systemKey)
                    MaterialAlertDialogBuilder(ctx)
                        .setTitle(status.titleRes)
                        .setMessage(status.messageRes)
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
                }
            } else {
                tvSystemHint.animate().cancel()
                tvSystemHint.visibility = View.GONE
            }

            // Controls
            if (item.isSystem) {
                // In System Category view, hide manual +/-.
                btnPlus.visibility = View.GONE
                btnMinus.visibility = View.GONE
                tvCounterValue.setPadding(tvCounterValue.paddingLeft, tvCounterValue.paddingTop, 0, tvCounterValue.paddingBottom)
            } else {
                btnPlus.visibility = View.VISIBLE
                btnMinus.visibility = View.VISIBLE

                btnPlus.isEnabled = item.canIncrease
                btnMinus.isEnabled = item.canDecrease
                btnPlus.alpha = 1f
                btnMinus.alpha = 1f

                btnPlus.setOnClickListener { onIncrement(item) }
                btnMinus.setOnClickListener { onDecrement(item) }
            }
        }
    }

    fun submitUiModels(list: List<CounterUiModel>) {
        submitList(list.map { CounterItem(it) })
    }
}
