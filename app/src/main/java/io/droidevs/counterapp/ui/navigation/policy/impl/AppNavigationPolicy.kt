package io.droidevs.counterapp.ui.navigation.policy.impl

import androidx.navigation.NavOptions
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.navigation.policy.NavigationPolicy

@Deprecated(
    message = "Deprecated: rootReplacementOptions was designed for a single root graph. Multi-backstack mode keeps per-tab NavHosts alive; prefer plain navigate()/back().",
    level = DeprecationLevel.WARNING
)
class AppNavigationPolicy : NavigationPolicy {

    override fun defaultOptions(): NavOptions =
        NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setRestoreState(true)
            .build()

    override fun rootReplacementOptions(): NavOptions =
        // nav_root isn't used in multi-backstack mode. Keep this for backward compatibility.
        NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setRestoreState(true)
            .build()
}
