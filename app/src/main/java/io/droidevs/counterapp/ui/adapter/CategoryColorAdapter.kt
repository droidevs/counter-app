package io.droidevs.counterapp.ui.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.model.CategoryColor

class CategoryColorAdapter(
    private val colors: MutableList<CategoryColor>,
    private val onColorSelected: (Int) -> Unit
) : RecyclerView.Adapter<CategoryColorAdapter.ColorVH>() {

    private var selectedColorIndex: Int = -1

    inner class ColorVH(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_color_picker, parent, false)
        return ColorVH(view)
    }

    override fun onBindViewHolder(holder: ColorVH, position: Int) {
        val item = colors[position]

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

    override fun getItemCount() = colors.size

    private fun selectColor(index: Int) {
        selectedColorIndex = index
        notifyDataSetChanged()
    }
}
