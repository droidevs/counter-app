package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.preference.counter.CounterDecrementStepPreference
import io.droidevs.counterapp.domain.preference.counter.CounterIncrementStepPreference
import io.droidevs.counterapp.domain.preference.counter.DefaultCounterValuePreference
import io.droidevs.counterapp.domain.preference.counter.MaximumCounterValuePreference
import io.droidevs.counterapp.domain.preference.counter.MinimumCounterValuePreference
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
    fun provideGetCounterIncrementStepUseCase(pref: CounterIncrementStepPreference): GetCounterIncrementStepUseCase =
        GetCounterIncrementStepUseCase(pref)

    @Provides
    @Singleton
    fun provideSetCounterIncrementStepUseCase(pref: CounterIncrementStepPreference): SetCounterIncrementStepUseCase =
        SetCounterIncrementStepUseCase(pref)

    @Provides
    @Singleton
    fun provideGetCounterDecrementStepUseCase(pref: CounterDecrementStepPreference): GetCounterDecrementStepUseCase =
        GetCounterDecrementStepUseCase(pref)

    @Provides
    @Singleton
    fun provideSetCounterDecrementStepUseCase(pref: CounterDecrementStepPreference): SetCounterDecrementStepUseCase =
        SetCounterDecrementStepUseCase(pref)

    @Provides
    @Singleton
    fun provideGetDefaultCounterValueUseCase(pref: DefaultCounterValuePreference): GetDefaultCounterValueUseCase =
        GetDefaultCounterValueUseCase(pref)

    @Provides
    @Singleton
    fun provideSetDefaultCounterValueUseCase(pref: DefaultCounterValuePreference): SetDefaultCounterValueUseCase =
        SetDefaultCounterValueUseCase(pref)

    @Provides
    @Singleton
    fun provideGetMinimumCounterValueUseCase(pref: MinimumCounterValuePreference): GetMinimumCounterValueUseCase =
        GetMinimumCounterValueUseCase(pref)

    @Provides
    @Singleton
    fun provideSetMinimumCounterValueUseCase(pref: MinimumCounterValuePreference): SetMinimumCounterValueUseCase =
        SetMinimumCounterValueUseCase(pref)

    @Provides
    @Singleton
    fun provideGetMaximumCounterValueUseCase(pref: MaximumCounterValuePreference): GetMaximumCounterValueUseCase =
        GetMaximumCounterValueUseCase(pref)

    @Provides
    @Singleton
    fun provideSetMaximumCounterValueUseCase(pref: MaximumCounterValuePreference): SetMaximumCounterValueUseCase =
        SetMaximumCounterValueUseCase(pref)
}
