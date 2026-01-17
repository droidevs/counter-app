package io.droidevs.counterapp.di.navigation

import androidx.fragment.app.FragmentActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import io.droidevs.counterapp.ui.navigation.AppNavigator
import io.droidevs.counterapp.ui.navigation.AppNavigatorImpl
import io.droidevs.counterapp.ui.navigation.host.NavHostProvider
import io.droidevs.counterapp.ui.navigation.host.impl.NavHostProviderImpl

@Module
@InstallIn(ActivityComponent::class)
object NavigationModule {

    @Provides
    fun provideNavHostProvider(
        activity: FragmentActivity
    ): NavHostProvider =
        NavHostProviderImpl(activity)

    @Provides
    fun provideAppNavigator(
        provider: NavHostProvider
    ): AppNavigator =
        AppNavigatorImpl(provider)
}
