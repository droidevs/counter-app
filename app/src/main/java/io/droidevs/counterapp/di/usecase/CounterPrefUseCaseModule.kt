package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.counter.CounterDecrementStepPreference
import io.droidevs.counterapp.domain.preference.counter.CounterIncrementStepPreference
import io.droidevs.counterapp.domain.preference.counter.DefaultCounterValuePreference
import io.droidevs.counterapp.domain.preference.counter.MaximumCounterValuePreference
import io.droidevs.counterapp.domain.preference.counter.MinimumCounterValuePreference
import io.droidevs.counterapp.domain.usecases.preference.CounterPreferenceUseCases
import io.droidevs.counterapp.domain.usecases.preference.counter.GetCounterDecrementStepUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetCounterIncrementStepUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetDefaultCounterValueUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetMaximumCounterValueUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.GetMinimumCounterValueUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.SetCounterDecrementStepUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.SetCounterIncrementStepUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.SetDefaultCounterValueUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.SetMaximumCounterValueUseCase
import io.droidevs.counterapp.domain.usecases.preference.counter.SetMinimumCounterValueUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CounterPrefUseCaseModule {

    @Provides
    @Singleton
    fun provideGetCounterIncrementStepUseCase(pref: CounterIncrementStepPreference, dispatchers: DispatcherProvider): GetCounterIncrementStepUseCase =
        GetCounterIncrementStepUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetCounterIncrementStepUseCase(pref: CounterIncrementStepPreference, dispatchers: DispatcherProvider): SetCounterIncrementStepUseCase =
        SetCounterIncrementStepUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideGetCounterDecrementStepUseCase(pref: CounterDecrementStepPreference, dispatchers: DispatcherProvider): GetCounterDecrementStepUseCase =
        GetCounterDecrementStepUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetCounterDecrementStepUseCase(pref: CounterDecrementStepPreference, dispatchers: DispatcherProvider): SetCounterDecrementStepUseCase =
        SetCounterDecrementStepUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideGetDefaultCounterValueUseCase(pref: DefaultCounterValuePreference, dispatchers: DispatcherProvider): GetDefaultCounterValueUseCase =
        GetDefaultCounterValueUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetDefaultCounterValueUseCase(pref: DefaultCounterValuePreference, dispatchers: DispatcherProvider): SetDefaultCounterValueUseCase =
        SetDefaultCounterValueUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideGetMinimumCounterValueUseCase(pref: MinimumCounterValuePreference, dispatchers: DispatcherProvider): GetMinimumCounterValueUseCase =
        GetMinimumCounterValueUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetMinimumCounterValueUseCase(pref: MinimumCounterValuePreference, dispatchers: DispatcherProvider): SetMinimumCounterValueUseCase =
        SetMinimumCounterValueUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideGetMaximumCounterValueUseCase(pref: MaximumCounterValuePreference, dispatchers: DispatcherProvider): GetMaximumCounterValueUseCase =
        GetMaximumCounterValueUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideSetMaximumCounterValueUseCase(pref: MaximumCounterValuePreference, dispatchers: DispatcherProvider): SetMaximumCounterValueUseCase =
        SetMaximumCounterValueUseCase(pref, dispatchers)

    @Provides
    @Singleton
    fun provideCounterPreferenceUseCases(
        getCounterIncrementStep: GetCounterIncrementStepUseCase,
        setCounterIncrementStep: SetCounterIncrementStepUseCase,
        getCounterDecrementStep: GetCounterDecrementStepUseCase,
        setCounterDecrementStep: SetCounterDecrementStepUseCase,
        getDefaultCounterValue: GetDefaultCounterValueUseCase,
        setDefaultCounterValue: SetDefaultCounterValueUseCase,
        getMinimumCounterValue: GetMinimumCounterValueUseCase,
        setMinimumCounterValue: SetMinimumCounterValueUseCase,
        getMaximumCounterValue: GetMaximumCounterValueUseCase,
        setMaximumCounterValue: SetMaximumCounterValueUseCase
    ): CounterPreferenceUseCases = CounterPreferenceUseCases(
        getCounterIncrementStep = getCounterIncrementStep,
        setCounterIncrementStep = setCounterIncrementStep,
        getCounterDecrementStep = getCounterDecrementStep,
        setCounterDecrementStep = setCounterDecrementStep,
        getDefaultCounterValue = getDefaultCounterValue,
        setDefaultCounterValue = setDefaultCounterValue,
        getMinimumCounterValue = getMinimumCounterValue,
        setMinimumCounterValue = setMinimumCounterValue,
        getMaximumCounterValue = getMaximumCounterValue,
        setMaximumCounterValue = setMaximumCounterValue
    )
}
