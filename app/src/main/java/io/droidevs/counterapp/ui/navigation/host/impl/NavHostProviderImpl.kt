package io.droidevs.counterapp.ui.navigation.host.impl

import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.navigation.host.NavHostProvider

class NavHostProviderImpl(
    private val activity: FragmentActivity
) : NavHostProvider {

    override fun navController(): NavController {
        // In multi-backstack mode, the active tab host is the primaryNavigationFragment.
        val primary = activity.supportFragmentManager.primaryNavigationFragment
        if (primary is NavHostFragment) return primary.navController

        // Fallback: find any NavHostFragment in the tab container.
        val container = activity.supportFragmentManager.findFragmentById(R.id.tab_nav_host_container)
        val host = when (container) {
            is NavHostFragment -> container
            else -> activity.supportFragmentManager.fragments.filterIsInstance<NavHostFragment>().firstOrNull()
        } ?: error("No NavHostFragment found. Ensure MainActivity created tab hosts.")

        return host.navController
    }
}
