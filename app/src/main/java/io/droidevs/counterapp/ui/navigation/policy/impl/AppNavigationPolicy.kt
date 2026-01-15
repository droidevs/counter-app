package io.droidevs.counterapp.ui.navigation.policy.impl

import androidx.navigation.NavOptions
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.navigation.policy.NavigationPolicy

class AppNavigationPolicy : NavigationPolicy {

    override fun defaultOptions(): NavOptions =
        NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setRestoreState(true)
            .build()

    override fun rootReplacementOptions(): NavOptions =
        NavOptions.Builder()
            .setPopUpTo(R.id.nav_root, inclusive = false, saveState = true)
            .setLaunchSingleTop(true)
            .setRestoreState(true)
            .build()
}
