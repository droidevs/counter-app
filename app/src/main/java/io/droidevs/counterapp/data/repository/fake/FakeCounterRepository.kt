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
import io.droidevs.counterapp.domain.system.SystemCategory
import io.droidevs.counterapp.domain.toDomain
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlin.collections.map

class FakeCounterRepository(
    val dummyData: DummyData
) : CounterRepository {

    private val countersFlow: Flow<List<Counter>> =
        DummyData.countersFlow.asStateFlow()
            .map { counters ->
                counters.map {
                    it.toDomain()
                }
            }
    // ------------------- Counter Operations -------------------

    override fun getLastEdited(limit: Int): Flow<List<Counter>> {
        return countersFlow.map { list ->
            list.filter { !it.isSystem }
                .sortedByDescending { it.orderAnchorAt }
                .take(limit)
        }
    }

    override fun getTotalCounters(): Flow<Int> {
        return countersFlow.map {
            it.filter { !it.isSystem }.size
        }
    }

    override suspend fun saveCounter(counter: Counter) {
        val index = DummyData.counters.indexOfFirst { it.id == counter.id }
        if (index != -1) {
            DummyData.counters[index] = counter.toEntity()
            DummyData.emitCounterUpdate()
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
        DummyData.emitCounterUpdate()
        DummyData.emitCategoryUpdate()
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
        DummyData.emitCounterUpdate()
        DummyData.emitCategoryUpdate()
    }

    override fun getCountersWithCategories(): Flow<List<CounterWithCategory>> {
        return countersFlow.map {
            it.filter {
                !it.isSystem
            }.sortedByDescending { it.orderAnchorAt }
                .map { counter ->
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
        return countersFlow.map { list ->
            var counters = list.filter { !it.isSystem }
                .sortedByDescending { it.orderAnchorAt }
                .take(limit)

            val categoriesMap = DummyData.categories.filter { !it.isSystem }
                .associateBy { it.id }

            counters.map { counter ->
                val category = categoriesMap[counter.categoryId]

                CounterWithCategory(
                    counter = counter,
                    category = category?.toDomain()
                )
            }
        }
    }

    override suspend fun seedDefaults() {
        DummyData.counters.addAll(
            DefaultData.buildCounters(
                existing = emptyMap(),
                // generate the list from categories dummy data that has kay != null
                categoryIdMap = DummyData.categories
                    .filter { it.kay != null }                    // Skip null kay
                .associate { category ->                       // Build the map
                    SystemCategory.valueOf(category.kay!!) to category.id
                }
            )
        )
    }

    override fun getSystemCounters(): Flow<List<Counter>> {
        return countersFlow.map { list ->
            list.filter { it.isSystem }
        }
    }

    override suspend fun incrementSystemCounter(counterKey: String) {
        DummyData.counters.indexOfFirst {
            it.kay == counterKey
        }.let { index ->
            if (index != -1) {
                val counter = dummyData.counters[index]
                val newCounter = counter.copy(
                    currentCount = counter.currentCount + 1
                )
                DummyData.counters[index] = newCounter
                DummyData.emitCounterUpdate()
            }
        }
    }

    override suspend fun updateSystemCounter(counterKey: String, count: Int) {
        DummyData.counters.indexOfFirst {
            it.kay == counterKey
        }.let { index ->
            if (index != -1) {
                val counter = dummyData.counters[index]
                val newCounter = counter.copy(
                    currentCount = count
                )
                DummyData.counters[index] = newCounter
                DummyData.emitCounterUpdate()
            }
        }
    }

    override fun getCounter(id: String): Flow<Counter?> {
        return countersFlow.map { list ->
            list.firstOrNull { it.id == id }
        }
    }


    override fun getAllCounters(): Flow<List<Counter>> =
        countersFlow

}
