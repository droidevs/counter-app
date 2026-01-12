package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    fun provideCreateCategoryUseCase(repository: CategoryRepository): CreateCategoryUseCase =
        CreateCategoryUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteCategoryUseCase(repository: CategoryRepository): DeleteCategoryUseCase =
        DeleteCategoryUseCase(repository)

    @Provides
    @Singleton
    fun provideGetAllCategoriesUseCase(repository: CategoryRepository): GetAllCategoriesUseCase =
        GetAllCategoriesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCategoryWithCountersUseCase(repository: CategoryRepository): GetCategoryWithCountersUseCase =
        GetCategoryWithCountersUseCase(repository)

    @Provides
    @Singleton
    fun provideGetExistingCategoryColorsUseCase(repository: CategoryRepository): GetExistingCategoryColorsUseCase =
        GetExistingCategoryColorsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetSystemCategoriesUseCase(repository: CategoryRepository): GetSystemCategoriesUseCase =
        GetSystemCategoriesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetTopCategoriesUseCase(repository: CategoryRepository): GetTopCategoriesUseCase =
        GetTopCategoriesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetTotalCategoriesCountUseCase(repository: CategoryRepository): GetTotalCategoriesCountUseCase =
        GetTotalCategoriesCountUseCase(repository)

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
