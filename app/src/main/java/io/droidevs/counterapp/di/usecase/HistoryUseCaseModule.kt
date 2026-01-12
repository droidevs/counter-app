package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.repository.HistoryRepository
import io.droidevs.counterapp.domain.usecases.history.AddHistoryEventUseCase
import io.droidevs.counterapp.domain.usecases.history.ClearHistoryUseCase
import io.droidevs.counterapp.domain.usecases.history.GetHistoryUseCase
import io.droidevs.counterapp.domain.usecases.history.HistoryUseCases
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HistoryUseCaseModule {

    @Provides
    @Singleton
    fun provideGetHistoryUseCase(repository: HistoryRepository): GetHistoryUseCase = GetHistoryUseCase(repository)

    @Provides
    @Singleton
    fun provideAddHistoryEventUseCase(repository: HistoryRepository): AddHistoryEventUseCase = AddHistoryEventUseCase(repository)

    @Provides
    @Singleton
    fun provideClearHistoryUseCase(repository: HistoryRepository): ClearHistoryUseCase = ClearHistoryUseCase(repository)

    @Provides
    @Singleton
    fun provideHistoryUseCases(getHistoryUseCase: GetHistoryUseCase, addHistoryEventUseCase: AddHistoryEventUseCase, clearHistoryUseCase: ClearHistoryUseCase): HistoryUseCases =
        HistoryUseCases(getHistoryUseCase, addHistoryEventUseCase, clearHistoryUseCase)
}
