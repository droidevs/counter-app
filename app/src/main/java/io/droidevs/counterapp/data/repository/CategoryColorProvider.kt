package io.droidevs.counterapp.data.repository

import android.content.Context
import android.graphics.Color
import androidx.core.graphics.ColorUtils
import com.google.android.material.color.MaterialColors
import io.droidevs.counterapp.domain.model.CategoryColor
import kotlin.math.abs

object CategoryColorProvider {

    private const val GOLDEN_RATIO = 0.618033988749895
    private const val SATURATION = 0.65f  // Slightly softer than 0.8 for better Material blend
    private const val BRIGHTNESS = 0.95f

    /**
     * Generates a consistent, visually distinct color for each category.
     * Same name → always same color.
     * Different names → very likely very different colors.
     */
    fun generateColorForCategory(context: Context, categoryName: String): CategoryColor {
        val hue = generateDistinctHue(categoryName)
        val baseColor = Color.HSVToColor(floatArrayOf(hue, SATURATION, BRIGHTNESS))

        // Blend with surface for proper Material Design elevation feel
        val surfaceColor = MaterialColors.getColor(context, com.google.android.material.R.attr.colorSurface, Color.WHITE)
        // 20% overlay feels good
        val color = MaterialColors.layer(surfaceColor, baseColor, 0.2f)
        return CategoryColor(color)
    }

    /**
     * Uses hash + golden ratio offset to ensure:
     * - Deterministic (same input → same hue)
     * - Well distributed across color wheel
     */
    private fun generateDistinctHue(categoryName: String): Float {
        // Use absolute hash to avoid negative values affecting distribution
        val hash = abs(categoryName.hashCode())
        val goldenOffset = hash * GOLDEN_RATIO
        val hue = (hash + goldenOffset) % 360f
        return hue.toFloat()
    }

    /**
     * Optional: Generate a full palette of distinct colors (for UI where you show suggestions)
     */
    fun generatePalette(context: Context, numColors: Int = 8): List<CategoryColor> {
        return List(numColors) { index ->
            val hue = (index * 360f * GOLDEN_RATIO) % 360f
            val baseColor = Color.HSVToColor(floatArrayOf(hue.toFloat(), SATURATION, BRIGHTNESS))
            val surface = MaterialColors.getColor(context, com.google.android.material.R.attr.colorSurface, Color.WHITE)
            MaterialColors.layer(surface, baseColor, 0.2f)
        }.map { CategoryColor(it) }
    }

    /**
     * Utility: Check if color is dark (for text color choice)
     */
    fun isDarkColor(color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) < 0.5
    }
}