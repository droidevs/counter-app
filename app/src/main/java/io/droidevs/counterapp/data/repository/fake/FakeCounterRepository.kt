package io.droidevs.counterapp.data.repository.fake

import android.util.Log
import io.droidevs.counterapp.data.DefaultData
import io.droidevs.counterapp.data.toDomain
import io.droidevs.counterapp.data.toEntity
import io.droidevs.counterapp.domain.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.model.CounterWithCategory
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.toDomain
import kotlinx.coroutines.flow.asStateFlow
import kotlin.collections.map

class FakeCounterRepository(
    val dummyData: DummyData
) : CounterRepository {

    private val countersFlow: Flow<List<Counter>> =
        dummyData.countersFlow.asStateFlow()
            .map { counters ->
                counters.map {
                    it.toDomain()
                }
            }
    // ------------------- Counter Operations -------------------

    override fun getLastEdited(limit: Int): Flow<List<Counter>> {
        return DummyData.countersFlow.map { list ->
            list.sortedByDescending { it.lastUpdatedAt }
                .take(limit)
                .map { it.toDomain() }
        }
    }

    override fun getTotalCounters(): Flow<Int> {
        return DummyData.countersFlow.map { it.size }
    }

    override suspend fun saveCounter(counter: Counter) {
        val index = DummyData.counters.indexOfFirst { it.id == counter.id }
        if (index != -1) {
            DummyData.counters[index] = counter.toEntity()
            dummyData.emitCounterUpdate()
        }
    }

    override suspend fun createCounter(counter: Counter) {
        DummyData.counters.add(counter.toEntity())
        val indexCategory = DummyData.categories.indexOfFirst { it.id == counter.categoryId }
        val category = DummyData.categories[indexCategory]
        val newCategory = category.copy(
            countersCount = category.countersCount + 1
        )
        DummyData.categories[indexCategory] = newCategory
        dummyData.emitCounterUpdate()
        dummyData.emitCategoryUpdate()
    }

    override suspend fun deleteCounter(counter: Counter) {
        DummyData.counters.removeIf { it.id == counter.id }
        Log.i("FakeCounterRepository", "Counter deleted: $counter")
        Log.i("Category id : ", "${counter.categoryId}")
        val indexCategory = DummyData.categories.indexOfFirst { it.id == counter.categoryId }
        val category = DummyData.categories[indexCategory]
        val newCategory = category.copy(
            countersCount = category.countersCount - 1
        )
        DummyData.categories[indexCategory] = newCategory
        dummyData.emitCounterUpdate()
        dummyData.emitCategoryUpdate()
    }

    override fun getCountersWithCategories(): Flow<List<CounterWithCategory>> {
        return countersFlow.map {
            it.map { counter ->
                val category = DummyData.categories.find { category ->
                    category.id == counter.categoryId
                }
                CounterWithCategory(
                    counter = counter,
                    category = category?.toDomain()
                )
            }
        }
    }

    override fun getLastEditedWithCategory(limit: Int): Flow<List<CounterWithCategory>> {
        return DummyData.countersFlow.map { list ->
            var counters = list.sortedByDescending { it.lastUpdatedAt }
                .take(limit)

            val categoriesMap = DummyData.categories.associateBy { it.id }
            counters.map { counter ->
                val category = categoriesMap[counter.categoryId]

                CounterWithCategory(
                    counter = counter.toDomain(),
                    category = category?.toDomain()
                )
            }
        }
    }

    override suspend fun seedDefaults() {
        DummyData.counters.addAll(
            DefaultData.defaultCounters
        )
    }

    override fun getSystemCounters(): Flow<List<Counter>> {
        return countersFlow.map { list ->
            list.filter { it.isSystem }
        }
    }


    override fun getAllCounters(): Flow<List<Counter>> =
        DummyData.countersFlow.map { counters ->
            counters.map { it.toDomain() }
        }

}
