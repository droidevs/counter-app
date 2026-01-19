package io.droidevs.counterapp.di.feedback

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.ui.feedback.CounterFeedbackErrorHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FeedbackModule {

    @Provides
    @Singleton
    fun provideCounterFeedbackErrorHandler(): CounterFeedbackErrorHandler = object : CounterFeedbackErrorHandler {
        override fun onSoundError(error: io.droidevs.counterapp.domain.sound.SoundError) {
            Log.w("CounterFeedback", "Sound error: $error")
        }

        override fun onVibrationError(error: io.droidevs.counterapp.domain.vibration.VibrationError) {
            Log.w("CounterFeedback", "Vibration error: $error")
        }
    }
}

