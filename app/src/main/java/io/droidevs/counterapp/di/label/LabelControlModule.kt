package io.droidevs.counterapp.di.label

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.ui.label.LabelControlErrorHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LabelControlModule {

    @Provides
    @Singleton
    fun provideLabelControlErrorHandler(): LabelControlErrorHandler =
        LabelControlErrorHandler {
            // No UI side effects; hook analytics/logging here later if needed.
        }
}


