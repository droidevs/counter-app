package io.droidevs.counterapp.domain.usecases.category

import io.droidevs.counterapp.domain.repository.CategoryRepository

data class CategoryUseCases(
    val createCategory: CreateCategoryUseCase,
    val deleteCategory: DeleteCategoryUseCase,
    val getAllCategories: GetAllCategoriesUseCase,
    val getCategoryWithCounters: GetCategoryWithCountersUseCase,
    val getExistingCategoryColors: GetExistingCategoryColorsUseCase,
    val getSystemCategories: GetSystemCategoriesUseCase,
    val getTopCategories: GetTopCategoriesUseCase,
    val getTotalCategoriesCount: GetTotalCategoriesCountUseCase
)
