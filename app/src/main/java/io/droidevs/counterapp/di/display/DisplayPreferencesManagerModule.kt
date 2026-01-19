package io.droidevs.counterapp.di.display

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.ui.display.DisplayPreferencesErrorHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DisplayPreferencesErrorHandlerModule {

    @Provides
    @Singleton
    fun provideDisplayPreferencesErrorHandler(): DisplayPreferencesErrorHandler =
        DisplayPreferencesErrorHandler {
            // No UI side effects; hook analytics/logging here later if needed.
        }
}
