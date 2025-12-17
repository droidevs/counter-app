package io.droidevs.counterapp.domain.repository

import kotlinx.coroutines.flow.Flow
import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.model.CategoryWithCounters

interface CategoryRepository {

    fun topCategories(limit: Int): Flow<List<Category>>

    fun getTotalCategoriesCount(): Flow<Int>

    fun categoryWithCounters(categoryId: String): Flow<CategoryWithCounters>

    fun allCategories(): Flow<List<Category>>

    suspend fun createCategory(category: Category)
}
