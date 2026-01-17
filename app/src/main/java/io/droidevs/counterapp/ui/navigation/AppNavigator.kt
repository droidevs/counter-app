package io.droidevs.counterapp.ui.navigation

import android.os.Bundle
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

/**
 * Navigation wrapper used by Fragments.
 *
 * In multi-backstack mode, the implementation delegates to the currently active tab's NavController
 * (via NavHostProviderImpl.primaryNavigationFragment).
 */
interface AppNavigator {

    fun navigate(
        actionId: Int,
        args: Bundle? = null,
        extras: Navigator.Extras? = null,
        options: NavOptions? = null
    )

    /**
     * Legacy API. In multi-backstack mode there is no single root graph to popUpTo.
     * This behaves the same as [navigate] unless explicit [options] are provided.
     */
    fun navigateRoot(
        actionId: Int,
        args: Bundle? = null,
        extras: Navigator.Extras? = null,
        options: NavOptions? = null
    )

    fun navigate(directions: NavDirections, options: NavOptions? = null)

    fun navigateRoot(directions: NavDirections, options: NavOptions? = null)

    fun back(): Boolean
}
