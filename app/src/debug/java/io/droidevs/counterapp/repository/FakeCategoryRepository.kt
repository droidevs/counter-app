package io.droidevs.counterapp.repository

import io.droidevs.counterapp.data.toDomain as counterEntityToDomain
import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.model.CategoryWithCounters
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class FakeCategoryRepository(
    val dummyData: DummyData
) : CategoryRepository {

    private val categoriesFlow: Flow<Result<List<Category>, DatabaseError>> =
        dummyData.categoriesFlow.asStateFlow()
            .map { categories ->
                Result.Success(categories.map { it.toDomain() })
            }

    override fun getCategory(categoryId: String): Flow<Result<Category, DatabaseError>> {
        return categoriesFlow.map { result ->
            when (result) {
                is Result.Success -> {
                    val category = result.data.firstOrNull { it.id == categoryId }
                    if (category != null) {
                        Result.Success(category)
                    } else {
                        Result.Failure(DatabaseError.NotFound)
                    }
                }

                is Result.Failure -> result
            }
        }
    }

    override fun topCategories(limit: Int): Flow<Result<List<Category>, DatabaseError>> {
        return categoriesFlow.map { result ->
            when (result) {
                is Result.Success -> Result.Success(
                    result.data.filter { !it.isSystem }
                        .sortedByDescending { it.countersCount }
                        .take(limit)
                )

                is Result.Failure -> result
            }
        }
    }

    override fun getTotalCategoriesCount(): Flow<Result<Int, DatabaseError>> {
        return categoriesFlow.map { result ->
            when (result) {
                is Result.Success -> Result.Success(result.data.filter { !it.isSystem }.size)
                is Result.Failure -> result
            }
        }
    }

    override fun categoryWithCounters(categoryId: String): Flow<Result<CategoryWithCounters, DatabaseError>> {
        return combine(dummyData.categoriesFlow, dummyData.countersFlow) { categories, counters ->
            val category = categories.firstOrNull { it.id == categoryId }
                ?: return@combine Result.Failure(DatabaseError.NotFound)

            val relatedCounters = counters.filter { it.categoryId == categoryId }
            Result.Success(
                CategoryWithCounters(
                    category = category.toDomain(),
                    counters = relatedCounters.map { it.counterEntityToDomain() }
                )
            )
        }
    }

    override fun allCategories(): Flow<Result<List<Category>, DatabaseError>> {
        return categoriesFlow.map { result ->
            when (result) {
                is Result.Success -> Result.Success(result.data.filter { !it.isSystem })
                is Result.Failure -> result
            }
        }
    }

    override suspend fun createCategory(category: Category): Result<Unit, DatabaseError> {
        dummyData.categories.add(category.toEntity())
        dummyData.emitCategoryUpdate()
        return Result.Success(Unit)
    }

    override suspend fun deleteCategory(categoryId: String): Result<Unit, DatabaseError> {
        dummyData.categories.removeIf { it.id == categoryId }
        dummyData.counters.forEach { entity ->
            if (entity.categoryId == categoryId) {
                val newCounter = entity.copy(categoryId = null)
                dummyData.counters[dummyData.counters.indexOf(entity)] = newCounter
            }
        }
        dummyData.emitCounterUpdate()
        dummyData.emitCategoryUpdate()
        return Result.Success(Unit)
    }

    override suspend fun getExistingCategoryColors(): Result<List<Int>, DatabaseError> {
        return Result.Success(dummyData.categories.map { it.color })
    }

    override fun getSystemCategories(): Flow<Result<List<Category>, DatabaseError>> {
        return categoriesFlow.map { result ->
            when (result) {
                is Result.Success -> Result.Success(result.data.filter { it.isSystem })
                is Result.Failure -> result
            }
        }
    }

    override suspend fun importCategories(categories: List<Category>): Result<Unit, DatabaseError> {
        val categoryEntities = categories.map { it.toEntity() }
        categoryEntities.forEach { categoryEntity ->
            val index = dummyData.categories.indexOfFirst { it.id == categoryEntity.id }
            if (index != -1) {
                dummyData.categories[index] = categoryEntity
            } else {
                dummyData.categories.add(categoryEntity)
            }
        }
        dummyData.emitCategoryUpdate()
        return Result.Success(Unit)
    }

    override suspend fun exportCategories(): Result<List<Category>, DatabaseError> {
        val categories = categoriesFlow.first()
        return when (categories) {
            is Result.Success -> Result.Success(categories.data.filter { !it.isSystem })
            is Result.Failure -> categories
        }
    }
}
