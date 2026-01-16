package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.services.FileImportService
import io.droidevs.counterapp.domain.usecases.importing.ImportUseCase
import io.droidevs.counterapp.domain.usecases.importing.ImportUseCases

@Module
@InstallIn(ViewModelComponent::class)
object ImportUseCaseModule {

    @Provides
    fun provideImportUseCase(
        fileImportService: FileImportService,
        counterRepository: CounterRepository,
        categoryRepository: CategoryRepository,
        dispatchers: DispatcherProvider
    ): ImportUseCase {
        return ImportUseCase(fileImportService, counterRepository, categoryRepository, dispatchers)
    }

    @Provides
    fun provideImportUseCases(importUseCase: ImportUseCase): ImportUseCases {
        return ImportUseCases(
            import = importUseCase
        )
    }
}
