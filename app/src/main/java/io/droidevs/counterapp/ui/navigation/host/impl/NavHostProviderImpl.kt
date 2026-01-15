package io.droidevs.counterapp.ui.navigation.host.impl

import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.navigation.host.NavHostProvider

class NavHostProviderImpl(
    private val activity: FragmentActivity
) : NavHostProvider {

    override fun navController(): NavController =
        (activity.supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
            .navController
}
