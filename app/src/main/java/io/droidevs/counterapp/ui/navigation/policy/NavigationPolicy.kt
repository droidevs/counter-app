package io.droidevs.counterapp.ui.navigation.policy

import androidx.navigation.NavOptions

@Deprecated(
    message = "Deprecated: with multi-backstack (one NavHost per tab) there is no single root graph; navigation policies based on popUpTo(nav_root) are no longer applicable.",
    level = DeprecationLevel.WARNING
)
interface NavigationPolicy {
    fun defaultOptions(): NavOptions
    fun rootReplacementOptions(): NavOptions
}
