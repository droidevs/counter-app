package io.droidevs.counterapp.di.hardware

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.ui.hardware.HardwareButtonControlErrorHandler

@Module
@InstallIn(SingletonComponent::class)
object HardwareButtonControlErrorHandlerModule {

    @Provides
    fun provideHardwareButtonControlErrorHandler(): HardwareButtonControlErrorHandler =
        HardwareButtonControlErrorHandler {
            Log.i("HardwareButtonControl", "Error reading preference: $it")
        }
}

