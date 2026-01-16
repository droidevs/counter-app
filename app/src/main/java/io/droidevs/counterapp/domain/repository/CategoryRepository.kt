package io.droidevs.counterapp.domain.repository

import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.model.CategoryWithCounters
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun getCategory(categoryId: String): Flow<Result<Category, DatabaseError>>

    fun topCategories(limit: Int): Flow<Result<List<Category>, DatabaseError>>

    fun getTotalCategoriesCount(): Flow<Result<Int, DatabaseError>>

    fun categoryWithCounters(categoryId: String): Flow<Result<CategoryWithCounters, DatabaseError>>

    fun allCategories(): Flow<Result<List<Category>, DatabaseError>>

    suspend fun createCategory(category: Category): Result<Unit, DatabaseError>
    suspend fun deleteCategory(categoryId: String): Result<Unit, DatabaseError>

    suspend fun getExistingCategoryColors(): Result<List<Int>, DatabaseError>

    fun getSystemCategories(): Flow<Result<List<Category>, DatabaseError>>

    suspend fun importCategories(categories: List<Category>): Result<Unit, DatabaseError>

    suspend fun exportCategories(): Result<List<Category>, DatabaseError>
}
