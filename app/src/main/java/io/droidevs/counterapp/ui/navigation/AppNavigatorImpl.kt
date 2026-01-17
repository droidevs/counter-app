package io.droidevs.counterapp.ui.navigation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import io.droidevs.counterapp.ui.navigation.host.NavHostProvider

class AppNavigatorImpl(
    private val navHostProvider: NavHostProvider
) : AppNavigator {

    private val navController: NavController
        get() = navHostProvider.navController()

    override fun navigate(
        actionId: Int,
        args: Bundle?,
        extras: Navigator.Extras?,
        options: NavOptions?
    ) {
        navController.navigate(actionId, args, options, extras)
    }

    override fun navigateRoot(
        actionId: Int,
        args: Bundle?,
        extras: Navigator.Extras?,
        options: NavOptions?
    ) {
        // No global root in multi-backstack mode. Behaves like navigate unless options provided.
        navController.navigate(actionId, args, options, extras)
    }

    override fun navigate(directions: NavDirections, options: NavOptions?) {
        if (options != null) navController.navigate(directions, options)
        else navController.navigate(directions)
    }

    override fun navigateRoot(directions: NavDirections, options: NavOptions?) {
        // No global root in multi-backstack mode. Behaves like navigate unless options provided.
        if (options != null) navController.navigate(directions, options)
        else navController.navigate(directions)
    }

    override fun back(): Boolean = navController.popBackStack()
}
