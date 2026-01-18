package io.droidevs.counterapp.ui.utils

import android.content.Context
import io.droidevs.counterapp.R
import io.droidevs.counterapp.data.repository.CategoryColorProvider

/**
 * Central place for the "No category" chip UI.
 *
 * - Label: [R.string.no_category]
 * - Color: generated with [CategoryColorProvider] so it blends with Material surface
 *   and stays consistent across themes.
 */
object NoCategoryUi {
    fun labelRes(): Int = R.string.no_category

    fun chipColor(context: Context): Int =
        CategoryColorProvider.generateColorForCategory(context, categoryName = "__NO_CATEGORY__").colorInt
}
