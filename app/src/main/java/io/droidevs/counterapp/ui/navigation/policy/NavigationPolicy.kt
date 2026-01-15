package io.droidevs.counterapp.ui.navigation.policy

import androidx.navigation.NavOptions

interface NavigationPolicy {
    fun defaultOptions(): NavOptions
    fun rootReplacementOptions(): NavOptions
}
