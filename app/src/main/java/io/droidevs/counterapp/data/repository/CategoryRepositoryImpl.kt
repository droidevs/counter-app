package io.droidevs.counterapp.data.repository

import io.droidevs.bmicalc.data.db.exceptions.DatabaseException
import io.droidevs.counterapp.data.dao.CategoryDao
import io.droidevs.counterapp.data.repository.exceptions.flowRunCatchingDatabase
import io.droidevs.counterapp.data.repository.exceptions.runCatchingDatabaseResult
import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.model.CategoryWithCounters
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(private val categoryDao: CategoryDao) : CategoryRepository {
    override fun getCategory(categoryId: String): Flow<Result<Category, DatabaseError>> =
        flowRunCatchingDatabase {
            categoryDao.getCategory(categoryId).map { it.toDomain() }
        }

    override fun topCategories(limit: Int): Flow<Result<List<Category>, DatabaseError>> =
        flowRunCatchingDatabase {
            categoryDao.getTopCategories(limit).map { list -> list.map { it.toDomain() } }
        }

    override fun getTotalCategoriesCount(): Flow<Result<Int, DatabaseError>> =
        flowRunCatchingDatabase {
            categoryDao.getTotalCategoriesCount()
        }

    override fun categoryWithCounters(categoryId: String): Flow<Result<CategoryWithCounters, DatabaseError>> =
        flowRunCatchingDatabase {
            categoryDao.getCategoryWithCounters(categoryId = categoryId).map { rows ->
                rows.firstOrNull()?.toDomain() ?: throw DatabaseException.NoElementFound()
            }
        }

    override suspend fun deleteCategory(categoryId: String): Result<Unit, DatabaseError> =
        runCatchingDatabaseResult {
            categoryDao.deleteCategory(categoryId)
        }

    override suspend fun getExistingCategoryColors(): Result<List<Int>, DatabaseError> =
        runCatchingDatabaseResult {
            categoryDao.getExistingCategoryColors()
        }

    override fun getSystemCategories(): Flow<Result<List<Category>, DatabaseError>> =
        flowRunCatchingDatabase {
            categoryDao.getSystemCategories().map { list -> list.map { it.toDomain() } }
        }

    override suspend fun importCategories(categories: List<Category>): Result<Unit, DatabaseError> =
        runCatchingDatabaseResult {
            categoryDao.insertAll(categories.map { it.toEntity() })
        }

    override suspend fun exportCategories(): Result<List<Category>, DatabaseError> =
        runCatchingDatabaseResult {
            categoryDao.getUserCategories().first().map { it.toDomain() }
        }

    override fun allCategories(): Flow<Result<List<Category>, DatabaseError>> =
        flowRunCatchingDatabase {
            categoryDao.getUserCategories().map { list -> list.map { it.toDomain() } }
        }

    override suspend fun createCategory(category: Category): Result<Unit, DatabaseError> =
        runCatchingDatabaseResult {
            categoryDao.insert(category.toEntity())
        }
}
