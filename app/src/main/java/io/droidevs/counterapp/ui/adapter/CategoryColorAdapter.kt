package io.droidevs.counterapp.ui.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.model.CategoryColor
import io.droidevs.counterapp.ui.adapter.base.DiffUpdate
import io.droidevs.counterapp.ui.adapter.models.CategoryColorItem

class CategoryColorAdapter(
    colors: MutableList<CategoryColor>,
    private val onColorSelected: (Int) -> Unit
) : RecyclerView.Adapter<CategoryColorAdapter.ColorVH>() {

    private val diff = DiffUpdate.diffable<CategoryColorItem>()

    private var items: MutableList<CategoryColorItem> = colors.map { CategoryColorItem(it) }.toMutableList()

    private var selectedColorIndex: Int = -1

    inner class ColorVH(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_color_picker, parent, false)
        return ColorVH(view)
    }

    override fun onBindViewHolder(holder: ColorVH, position: Int) {
        val item = items[position].model

        holder.view.background = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(item.colorInt)
            if (selectedColorIndex == position) {
                val color = holder.view.context.getColor(com.google.android.material.R.color.design_default_color_primary)
                setStroke(6, color)
            }
        }

        holder.view.setOnClickListener {
            selectColor(position)
            onColorSelected(item.colorInt)
        }
    }

    override fun getItemCount() = items.size

    private fun selectColor(index: Int) {
        val previous = selectedColorIndex
        selectedColorIndex = index

        if (previous != -1) notifyItemChanged(previous)
        notifyItemChanged(index)
    }

    fun updateColors(newColors: List<CategoryColor>) {
        val old = items.toList()
        val newItems = newColors.map { CategoryColorItem(it) }

        items = newItems.toMutableList()
        diff.apply(adapter = this, old = old, new = newItems)

        // Keep selection consistent if colors list changed.
        if (selectedColorIndex >= items.size) {
            selectedColorIndex = -1
        }
    }
}
