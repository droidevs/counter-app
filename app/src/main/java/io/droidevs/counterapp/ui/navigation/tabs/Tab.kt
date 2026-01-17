package io.droidevs.counterapp.ui.navigation.tabs

import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import io.droidevs.counterapp.R

/**
 * BottomNav / NavigationRail tabs.
 * Each tab owns its own NavHostFragment + back stack.
 */
enum class Tab(
    @IdRes val menuId: Int,
    @NavigationRes val graphResId: Int
) {
    HOME(R.id.home_graph, R.navigation.nav_home),
    COUNTERS(R.id.counters_graph, R.navigation.nav_counters),
    CATEGORIES(R.id.categories_graph, R.navigation.nav_categories),
    SETTINGS(R.id.settings_graph, R.navigation.nav_settings);

    companion object {
        fun fromMenuId(@IdRes menuId: Int): Tab? = entries.firstOrNull { it.menuId == menuId }
    }
}
