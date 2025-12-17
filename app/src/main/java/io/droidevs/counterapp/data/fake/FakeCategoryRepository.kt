package io.droidevs.counterapp.data.fake

import io.droidevs.counterapp.data.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.model.CategoryWithCounters
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toEntity
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class FakeCategoryRepository : CategoryRepository {

    // In-memory "database"
    private val categoriesData =
        DummyData.categories.toMutableList()

    // Reactive stream (Room-like behavior)
    private val _categoriesFlow =
        MutableStateFlow(categoriesData.toList())

    private val _countersFlow =
        MutableStateFlow(DummyData.counters.toList())

    private val categoriesFlow: Flow<List<Category>> =
        _categoriesFlow.asStateFlow()
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
        return combine(_categoriesFlow, _countersFlow) { categories, counters ->

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
        }
    }

    override fun allCategories(): Flow<List<Category>> {
        return categoriesFlow
    }

    override suspend fun createCategory(category: Category) {
        categoriesData.add(category.toEntity())
        emitUpdate()
    }

    // ---------------- Helpers ----------------

    private fun emitUpdate() {
        _categoriesFlow.value = categoriesData.toList()
    }
}
