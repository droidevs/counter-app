package io.droidevs.counterapp.di.feedback

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.feedback.CounterFeedbackManager
import io.droidevs.counterapp.domain.usecases.sound.PlayCounterSoundUseCase
import io.droidevs.counterapp.domain.usecases.vibration.VibrateCounterUseCase
import io.droidevs.counterapp.ui.feedback.CounterFeedbackErrorHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CounterFeedbackManagerModule {

    @Provides
    @Singleton
    fun provideCounterFeedbackManager(
        playSound: PlayCounterSoundUseCase,
        vibrate: VibrateCounterUseCase,
        errorHandler: CounterFeedbackErrorHandler,
    ): CounterFeedbackManager = CounterFeedbackManager(
        playSound = playSound,
        vibrate = vibrate,
        errorHandler = errorHandler,
    )
}

