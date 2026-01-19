package io.droidevs.counterapp.di.theme

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.theme.AppCompatThemeApplier
import io.droidevs.counterapp.data.theme.ThemeObserverImpl
import io.droidevs.counterapp.domain.theme.ThemeApplier
import io.droidevs.counterapp.domain.theme.ThemeObserver
import io.droidevs.counterapp.domain.usecases.preference.display.GetThemeUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ThemeModule {


    @Provides
    @Singleton
    fun providesThemeApplier(): ThemeApplier = AppCompatThemeApplier()

    @Provides
    @Singleton
    fun providesThemeObserver(
        getThemeUseCase: GetThemeUseCase,
        themeApplier: ThemeApplier
    ): ThemeObserver =
        ThemeObserverImpl(
            getThemeUseCase = getThemeUseCase,
            applier = themeApplier
        )
}

