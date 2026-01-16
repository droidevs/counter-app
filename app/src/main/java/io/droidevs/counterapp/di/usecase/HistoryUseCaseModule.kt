package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
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
    fun provideGetHistoryUseCase(repository: HistoryRepository, dispatchers: DispatcherProvider): GetHistoryUseCase = GetHistoryUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideAddHistoryEventUseCase(repository: HistoryRepository, dispatchers: DispatcherProvider): AddHistoryEventUseCase = AddHistoryEventUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideClearHistoryUseCase(repository: HistoryRepository, dispatchers: DispatcherProvider): ClearHistoryUseCase = ClearHistoryUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideHistoryUseCases(getHistoryUseCase: GetHistoryUseCase, addHistoryEventUseCase: AddHistoryEventUseCase, clearHistoryUseCase: ClearHistoryUseCase): HistoryUseCases =
        HistoryUseCases(getHistoryUseCase, addHistoryEventUseCase, clearHistoryUseCase)
}
