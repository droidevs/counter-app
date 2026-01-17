package io.droidevs.counterapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.ItemCategoryCounterBinding
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.system.SystemCounterSupportStatus
import io.droidevs.counterapp.ui.system.SystemCounterSupportUi

class CategoryCountersAdapter(
    private val onIncrement: (CounterUiModel) -> Unit = {},
    private val onDecrement: (CounterUiModel) -> Unit = {}
) : ListAdapter<CounterUiModel, CategoryCountersAdapter.ViewHolder>(Diff()) {

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
        val item = getItem(position)
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
            val allowControls = if (item.isSystem && hint != null) {
                SystemCounterSupportStatus.evaluate(ctx, item.systemKey).isSupported
            } else true

            btnPlus.isEnabled = allowControls
            btnMinus.isEnabled = allowControls
            btnPlus.alpha = if (allowControls) 1f else 0.5f
            btnMinus.alpha = if (allowControls) 1f else 0.5f

            btnPlus.setOnClickListener { if (allowControls) onIncrement(item) }
            btnMinus.setOnClickListener { if (allowControls) onDecrement(item) }
        }
    }

    class Diff : DiffUtil.ItemCallback<CounterUiModel>() {
        override fun areItemsTheSame(old: CounterUiModel, new: CounterUiModel) =
            old.id == new.id

        override fun areContentsTheSame(old: CounterUiModel, new: CounterUiModel) =
            old == new
    }
}
