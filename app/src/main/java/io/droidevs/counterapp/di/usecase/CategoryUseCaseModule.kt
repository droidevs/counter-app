package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.domain.usecases.category.CreateCategoryUseCase
import io.droidevs.counterapp.domain.usecases.category.DeleteCategoryUseCase
import io.droidevs.counterapp.domain.usecases.category.GetAllCategoriesUseCase
import io.droidevs.counterapp.domain.usecases.category.GetCategoryWithCountersUseCase
import io.droidevs.counterapp.domain.usecases.category.GetExistingCategoryColorsUseCase
import io.droidevs.counterapp.domain.usecases.category.GetSystemCategoriesUseCase
import io.droidevs.counterapp.domain.usecases.category.GetTopCategoriesUseCase
import io.droidevs.counterapp.domain.usecases.category.GetTotalCategoriesCountUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CategoryUseCaseModule {

    @Provides
    @Singleton
    fun provideCreateCategoryUseCase(repository: CategoryRepository, dispatchers: DispatcherProvider): CreateCategoryUseCase =
        CreateCategoryUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideDeleteCategoryUseCase(repository: CategoryRepository, dispatchers: DispatcherProvider): DeleteCategoryUseCase =
        DeleteCategoryUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideGetAllCategoriesUseCase(repository: CategoryRepository, dispatchers: DispatcherProvider): GetAllCategoriesUseCase =
        GetAllCategoriesUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideGetCategoryWithCountersUseCase(repository: CategoryRepository, dispatchers: DispatcherProvider): GetCategoryWithCountersUseCase =
        GetCategoryWithCountersUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideGetExistingCategoryColorsUseCase(repository: CategoryRepository, dispatchers: DispatcherProvider): GetExistingCategoryColorsUseCase =
        GetExistingCategoryColorsUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideGetSystemCategoriesUseCase(repository: CategoryRepository, dispatchers: DispatcherProvider): GetSystemCategoriesUseCase =
        GetSystemCategoriesUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideGetTopCategoriesUseCase(repository: CategoryRepository, dispatchers: DispatcherProvider): GetTopCategoriesUseCase =
        GetTopCategoriesUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideGetTotalCategoriesCountUseCase(repository: CategoryRepository, dispatchers: DispatcherProvider): GetTotalCategoriesCountUseCase =
        GetTotalCategoriesCountUseCase(repository, dispatchers)

    @Provides
    @Singleton
    fun provideCategoryUseCases(
        createCategory: CreateCategoryUseCase,
        deleteCategory: DeleteCategoryUseCase,
        getAllCategories: GetAllCategoriesUseCase,
        getCategoryWithCounters: GetCategoryWithCountersUseCase,
        getExistingCategoryColors: GetExistingCategoryColorsUseCase,
        getSystemCategories: GetSystemCategoriesUseCase,
        getTopCategories: GetTopCategoriesUseCase,
        getTotalCategoriesCount: GetTotalCategoriesCountUseCase
    ): CategoryUseCases = CategoryUseCases(
        createCategory,
        deleteCategory,
        getAllCategories,
        getCategoryWithCounters,
        getExistingCategoryColors,
        getSystemCategories,
        getTopCategories,
        getTotalCategoriesCount
    )
}
