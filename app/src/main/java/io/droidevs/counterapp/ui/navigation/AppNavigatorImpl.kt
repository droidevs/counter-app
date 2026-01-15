package io.droidevs.counterapp.ui.navigation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.savedstate.savedState
import io.droidevs.counterapp.ui.navigation.host.NavHostProvider
import io.droidevs.counterapp.ui.navigation.policy.NavigationPolicy

class AppNavigatorImpl(
    private val navHostProvider: NavHostProvider,
    private val policy: NavigationPolicy
) : AppNavigator {

    private val navController: NavController
        get() = navHostProvider.navController()

    override fun navigate(
        actionId: Int,
        args: Bundle?,
        extras: Navigator.Extras?,
        options: NavOptions?
    ) {
        navController.navigate(
            actionId,
            args,
            options ?: policy.defaultOptions(),
            extras
        )
    }

    override fun navigateRoot(
        actionId: Int,
        args: Bundle?,
        extras: Navigator.Extras?,
        options: NavOptions?
    ) {
        navController.navigate(
            actionId,
            args,
            options ?: policy.rootReplacementOptions(),
            extras
        )
    }

    override fun navigate(directions: NavDirections) {
        navController.navigate(
            directions,
            policy.defaultOptions()
        )
    }

    override fun back() {
        navController.popBackStack()
    }
}
