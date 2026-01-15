package io.droidevs.counterapp.ui.navigation

import android.os.Bundle
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

interface AppNavigator {

    fun navigate(
        actionId: Int,
        args: Bundle? = null,
        extras: Navigator.Extras? = null,
        options: NavOptions? = null
    )

    fun navigateRoot(
        actionId: Int,
        args: Bundle? = null,
        extras: Navigator.Extras? = null,
        options: NavOptions? = null
    )

    fun navigate(directions: NavDirections)

    fun navigateRoot(directions: NavDirections)

    fun back()
}
