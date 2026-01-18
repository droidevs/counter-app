package io.droidevs.counterapp.di.hardware

import androidx.fragment.app.FragmentActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import io.droidevs.counterapp.ui.hardware.ActiveContentFragmentProvider
import io.droidevs.counterapp.ui.hardware.HardwareButtonKeyDispatcher

@Module
@InstallIn(ActivityComponent::class)
object HardwareButtonModule {

    @Provides
    fun provideHardwareButtonKeyDispatcher(): HardwareButtonKeyDispatcher = HardwareButtonKeyDispatcher()

    @Provides
    fun provideActiveContentFragmentProvider(activity: FragmentActivity): ActiveContentFragmentProvider =
        ActiveContentFragmentProvider(activity)
}

