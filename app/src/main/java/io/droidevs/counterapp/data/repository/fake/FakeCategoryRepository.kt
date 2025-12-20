package io.droidevs.counterapp.data.repository.fake

import io.droidevs.counterapp.data.DefaultData
import io.droidevs.counterapp.data.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.model.CategoryWithCounters
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toEntity
import kotlinx.coroutines.flow.combine

class FakeCategoryRepository(
    val dummyData: DummyData
) : CategoryRepository {


    private val categoriesFlow: Flow<List<Category>> =
        dummyData.categoriesFlow.asStateFlow()
            .map { categories ->
                categories.map {
                    it.toDomain()
                }
            }

    // ---------------- Public API ----------------

    override fun topCategories(limit: Int): Flow<List<Category>> {
        return categoriesFlow.map { list ->
            list.sortedByDescending { it.countersCount }
                .take(limit)
        }
    }

    override fun getTotalCategoriesCount(): Flow<Int> {
        return categoriesFlow.map {
            it.size
        }
    }

    override fun categoryWithCounters(categoryId: String): Flow<CategoryWithCounters> {
        return combine(dummyData.categoriesFlow, dummyData.countersFlow) { categories, counters ->

            try {
                val category = categories.first {
                    it.id == categoryId
                }

                val relatedCounters = counters.filter {
                    it.categoryId == categoryId
                }

                CategoryWithCounters(
                    category = category.toDomain(),
                    counters = relatedCounters.map { it.toDomain() }
                )
            } catch (e : NoSuchElementException) {

                CategoryWithCounters.default()
            }
        }
    }

    override fun allCategories(): Flow<List<Category>> {
        return categoriesFlow
    }

    override suspend fun createCategory(category: Category) {
        dummyData.categories.add(category.toEntity())
        dummyData.emitCategoryUpdate()
    }

    override fun deleteCategory(categoryId: String) {
        dummyData.categories.removeIf { it.id == categoryId }
        // delete all counters related with that category or set category id to null
        dummyData.counters.forEach {
            if (it.categoryId == categoryId) {
                val newCounter = it.copy(
                    categoryId = null
                )
                dummyData.counters[dummyData.counters.indexOf(it)] = newCounter
            }
            dummyData.emitCounterUpdate()
        }
        dummyData.emitCategoryUpdate()
    }

    override suspend fun getExistingCategoryColors(): List<Int> {
        return dummyData.categories.map { it.color }
    }

    override suspend fun seedDefaults() {
        dummyData.categories.addAll(
            DefaultData.defaultCategories
        )
        dummyData.emitCategoryUpdate()
    }

    override fun getSystemCategories(): Flow<List<Category>> {
        return categoriesFlow.map { list ->
            list.filter { it.isSystem }
        }
    }

}
