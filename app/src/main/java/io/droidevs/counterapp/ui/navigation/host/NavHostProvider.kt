package io.droidevs.counterapp.ui.navigation.host

import androidx.navigation.NavController

interface NavHostProvider {
    fun navController(): NavController
}