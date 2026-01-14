package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.services.FileExportService
import io.droidevs.counterapp.domain.services.FileImportService
import io.droidevs.counterapp.domain.usecases.export.ExportCountersUseCase
import io.droidevs.counterapp.domain.usecases.export.ExportUseCases
import io.droidevs.counterapp.domain.usecases.export.GetAvailableExportFormatsUseCase
import io.droidevs.counterapp.domain.usecases.import.ImportCountersUseCase
import io.droidevs.counterapp.domain.usecases.import.ImportUseCases

@Module
@InstallIn(ViewModelComponent::class)
object ExportUseCaseModule {

    @Provides
    fun provideExportCountersUseCase(
        counterRepository: CounterRepository,
        fileExportService: FileExportService
    ): ExportCountersUseCase {
        return ExportCountersUseCase(counterRepository, fileExportService)
    }

    @Provides
    fun provideGetAvailableExportFormatsUseCase(fileExportService: FileExportService): GetAvailableExportFormatsUseCase {
        return GetAvailableExportFormatsUseCase(fileExportService)
    }

    @Provides
    fun provideExportUseCases(
        exportCountersUseCase: ExportCountersUseCase,
        getAvailableExportFormatsUseCase: GetAvailableExportFormatsUseCase
    ): ExportUseCases {
        return ExportUseCases(
            exportCounters = exportCountersUseCase,
            getAvailableExportFormats = getAvailableExportFormatsUseCase
        )
    }

    @Provides
    fun provideImportCountersUseCase(
        counterRepository: CounterRepository,
        fileImportService: FileImportService
    ): ImportCountersUseCase {
        return ImportCountersUseCase(counterRepository, fileImportService)
    }

    @Provides
    fun provideImportUseCases(importCountersUseCase: ImportCountersUseCase): ImportUseCases {
        return ImportUseCases(
            importCounters = importCountersUseCase
        )
    }
}