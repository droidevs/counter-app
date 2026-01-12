package io.droidevs.counterapp.di.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.preference.impl.counter.CounterDecrementStepPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.counter.CounterIncrementStepPreferenceImpl
import io.droidevs.counterapp.data.preference.impl.counter.DefaultCounterValuePreferenceImpl
import io.droidevs.counterapp.data.preference.impl.counter.MaximumCounterValuePreferenceImpl
import io.droidevs.counterapp.data.preference.impl.counter.MinimumCounterValuePreferenceImpl
import io.droidevs.counterapp.domain.preference.counter.CounterDecrementStepPreference
import io.droidevs.counterapp.domain.preference.counter.CounterIncrementStepPreference
import io.droidevs.counterapp.domain.preference.counter.DefaultCounterValuePreference
import io.droidevs.counterapp.domain.preference.counter.MaximumCounterValuePreference
import io.droidevs.counterapp.domain.preference.counter.MinimumCounterValuePreference
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CounterPreferenceModule {

    @Provides
    @Singleton
    fun provideCounterIncrementStepPreference(dataStore: DataStore<Preferences>): CounterIncrementStepPreference =
        CounterIncrementStepPreferenceImpl(dataStore)

    @Provides
    @Singleton
    fun provideCounterDecrementStepPreference(dataStore: DataStore<Preferences>): CounterDecrementStepPreference =
        CounterDecrementStepPreferenceImpl(dataStore)

    @Provides
    @Singleton
    fun provideDefaultCounterValuePreference(dataStore: DataStore<Preferences>): DefaultCounterValuePreference =
        DefaultCounterValuePreferenceImpl(dataStore)

    @Provides
    @Singleton
    fun provideMaximumCounterValuePreference(dataStore: DataStore<Preferences>): MaximumCounterValuePreference =
        MaximumCounterValuePreferenceImpl(dataStore)

    @Provides
    @Singleton
    fun provideMinimumCounterValuePreference(dataStore: DataStore<Preferences>): MinimumCounterValuePreference =
        MinimumCounterValuePreferenceImpl(dataStore)
}
