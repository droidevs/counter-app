package io.droidevs.counterapp.di.display

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.data.display.DisplayPreferencesProviderImpl
import io.droidevs.counterapp.domain.display.DisplayPreferencesProvider
import io.droidevs.counterapp.domain.usecases.preference.display.GetHideControlsUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.GetHideLastUpdateUseCase
import io.droidevs.counterapp.domain.usecases.preference.display.GetHideCounterCategoryLabelUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DisplayPreferencesProviderModule {

    @Provides
    @Singleton
    fun providesDisplayPreferencesProvider(
        getHideControlsUseCase: GetHideControlsUseCase,
        getHideLastUpdateUseCase: GetHideLastUpdateUseCase,
        getHideCounterCategoryLabelUseCase: GetHideCounterCategoryLabelUseCase
    ): DisplayPreferencesProvider =
        DisplayPreferencesProviderImpl(
            getHideControls = getHideControlsUseCase,
            getHideLastUpdate = getHideLastUpdateUseCase,
            getHideCounterCategoryLabel = getHideCounterCategoryLabelUseCase
        )
}
