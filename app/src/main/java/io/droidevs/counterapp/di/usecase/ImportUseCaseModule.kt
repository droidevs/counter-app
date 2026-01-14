package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.services.FileImportService
import io.droidevs.counterapp.domain.usecases.import.ImportUseCase
import io.droidevs.counterapp.domain.usecases.import.ImportUseCases

@Module
@InstallIn(ViewModelComponent::class)
object ImportUseCaseModule {

    @Provides
    fun provideImportUseCase(
        fileImportService: FileImportService,
        counterRepository: CounterRepository,
        categoryRepository: CategoryRepository
    ): ImportUseCase {
        return ImportUseCase(fileImportService, counterRepository, categoryRepository)
    }

    @Provides
    fun provideImportUseCases(importUseCase: ImportUseCase): ImportUseCases {
        return ImportUseCases(
            import = importUseCase
        )
    }
}