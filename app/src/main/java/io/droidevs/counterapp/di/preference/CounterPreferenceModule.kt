package io.droidevs.counterapp.di.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.BuildConfig
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
import io.droidevs.counterapp.preference.counter.DummyCounterDecrementStepPreference
import io.droidevs.counterapp.preference.counter.DummyCounterIncrementStepPreference
import io.droidevs.counterapp.preference.counter.DummyDefaultCounterValuePreference
import io.droidevs.counterapp.preference.counter.DummyMaximumCounterValuePreference
import io.droidevs.counterapp.preference.counter.DummyMinimumCounterValuePreference
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CounterPreferenceModule {

    @Provides
    @Singleton
    fun provideCounterIncrementStepPreference(dataStore: Lazy<DataStore<Preferences>>): CounterIncrementStepPreference = 
        if (BuildConfig.DEBUG) {
            DummyCounterIncrementStepPreference()
        } else {
            CounterIncrementStepPreferenceImpl(dataStore.get())
        }

    @Provides
    @Singleton
    fun provideCounterDecrementStepPreference(dataStore: Lazy<DataStore<Preferences>>): CounterDecrementStepPreference = 
        if (BuildConfig.DEBUG) {
            DummyCounterDecrementStepPreference()
        } else {
            CounterDecrementStepPreferenceImpl(dataStore.get())
        }

    @Provides
    @Singleton
    fun provideDefaultCounterValuePreference(dataStore: Lazy<DataStore<Preferences>>): DefaultCounterValuePreference = 
        if (BuildConfig.DEBUG) {
            DummyDefaultCounterValuePreference()
        } else {
            DefaultCounterValuePreferenceImpl(dataStore.get())
        }

    @Provides
    @Singleton
    fun provideMaximumCounterValuePreference(dataStore: Lazy<DataStore<Preferences>>): MaximumCounterValuePreference = 
        if (BuildConfig.DEBUG) {
            DummyMaximumCounterValuePreference()
        } else {
            MaximumCounterValuePreferenceImpl(dataStore.get())
        }

    @Provides
    @Singleton
    fun provideMinimumCounterValuePreference(dataStore: Lazy<DataStore<Preferences>>): MinimumCounterValuePreference = 
        if (BuildConfig.DEBUG) {
            DummyMinimumCounterValuePreference()
        } else {
            MinimumCounterValuePreferenceImpl(dataStore.get())
        }
}
