package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.services.FileExportService
import io.droidevs.counterapp.domain.usecases.export.ExportUseCase
import io.droidevs.counterapp.domain.usecases.export.ExportUseCases
import io.droidevs.counterapp.domain.usecases.export.GetAvailableExportFormatsUseCase

@Module
@InstallIn(ViewModelComponent::class)
object ExportUseCaseModule {

    @Provides
    fun provideExportUseCase(
        counterRepository: CounterRepository,
        categoryRepository: CategoryRepository,
        fileExportService: FileExportService,
        dispatchers: DispatcherProvider
    ): ExportUseCase {
        return ExportUseCase(counterRepository, categoryRepository, fileExportService, dispatchers)
    }

    @Provides
    fun provideGetAvailableExportFormatsUseCase(
        fileExportService: FileExportService,
        dispatchers: DispatcherProvider
    ): GetAvailableExportFormatsUseCase {
        return GetAvailableExportFormatsUseCase(fileExportService, dispatchers)
    }

    @Provides
    fun provideExportUseCases(
        exportUseCase: ExportUseCase,
        getAvailableExportFormatsUseCase: GetAvailableExportFormatsUseCase
    ): ExportUseCases {
        return ExportUseCases(
            export = exportUseCase,
            getAvailableExportFormats = getAvailableExportFormatsUseCase
        )
    }
}
