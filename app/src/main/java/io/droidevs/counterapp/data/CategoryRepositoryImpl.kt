package io.droidevs.counterapp.data

import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(private val categoryDao: CategoryDao) : CategoryRepository{

    // Get top 3 categories (ordered by countersCount)
    override fun topCategories(limit: Int) : Flow<List<Category>> {
        return categoryDao.getTopCategories(limit).map {
            list -> list.map { it.toDomain() }
        }
    }

    override fun getTotalCategoriesCount(): Flow<Int> {
        return categoryDao.getTotalCategoriesCount()
    }

    // Get all categories
    override fun allCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun createCategory(category: Category) {
        categoryDao.insert(category.toEntity())
    }
}
