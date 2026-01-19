package io.droidevs.counterapp.di.sound

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.sound.DummyCounterSoundPlayer
import io.droidevs.counterapp.domain.sound.CounterSoundPlayer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SoundModule {

    @Binds
    @Singleton
    abstract fun bindCounterSoundPlayer(impl: DummyCounterSoundPlayer): CounterSoundPlayer
}

