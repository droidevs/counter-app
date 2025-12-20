package io.droidevs.counterapp.ui.utils

import android.content.Context
import android.graphics.Color
import com.google.android.material.R
import com.google.android.material.color.MaterialColors
import io.droidevs.counterapp.domain.model.Category

object CategoryColorUtil {


    fun generateColor(context : Context, category : Category) : Int {
        val baseColor = generateBaseColor(category.name)
        val blendedColor = materialBlend(context, baseColor)
        return blendedColor
    }

    private fun generateBaseColor(key: String): Int {
        val hash = key.hashCode()
        val hue = (hash % 360 + 360) % 360

        return Color.HSVToColor(
            floatArrayOf(
                hue.toFloat(), // Hue
                0.45f,         // Saturation (soft)
                0.90f          // Brightness
            )
        )
    }

    private fun materialBlend(context: Context, color: Int): Int {
        val surface = MaterialColors.getColor(
            context,
            R.attr.colorSurface,
            Color.WHITE
        )
        return MaterialColors.layer(surface, color)
    }

    fun isDark(color: Int): Boolean {
        val darkness =
            1 - (0.299 * Color.red(color)
                    + 0.587 * Color.green(color)
                    + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }

}