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
    @NavigationRes val graphResId: Int,
    @IdRes val startDestinationId: Int
) {
    HOME(R.id.home_graph, R.navigation.nav_home, R.id.homeFragment),
    COUNTERS(R.id.counters_graph, R.navigation.nav_counters, R.id.counterListFragment),
    CATEGORIES(R.id.categories_graph, R.navigation.nav_categories, R.id.categoryListFragment),
    SETTINGS(R.id.settings_graph, R.navigation.nav_settings, R.id.settingsFragment);

    companion object {
        fun fromMenuId(@IdRes menuId: Int): Tab? = entries.firstOrNull { it.menuId == menuId }
    }
}
