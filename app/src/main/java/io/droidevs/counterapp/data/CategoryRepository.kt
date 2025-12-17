package io.droidevs.counterapp.data

import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepository(private val categoryDao: CategoryDao) {

    // Get top 3 categories (ordered by countersCount)
    fun topCategories(limit: Int) : Flow<List<Category>> {
        return categoryDao.getTopCategories(limit).map {
            list -> list.map { it.toDomain() }
        }
    }

    // Get all categories
    fun allCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun createCategory(category: Category) {
        categoryDao.insert(category.toEntity())
    }


}
