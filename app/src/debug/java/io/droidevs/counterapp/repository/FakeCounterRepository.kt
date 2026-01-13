package io.droidevs.counterapp.repository

import android.util.Log
import io.droidevs.counterapp.data.toDomain
import io.droidevs.counterapp.data.toEntity
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.model.CounterWithCategory
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlin.collections.get

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
        val index = dummyData.counters.indexOfFirst { it.id == counter.id }
        if (index != -1) {
            dummyData.counters[index] = counter.toEntity()
            dummyData.emitCounterUpdate()
        }
    }

    override suspend fun createCounter(counter: Counter) {
        dummyData.counters.add(counter.toEntity())
        val indexCategory = dummyData.categories.indexOfFirst { it.id == counter.categoryId }
        val category = dummyData.categories[indexCategory]
        val newCategory = category.copy(
            countersCount = category.countersCount + 1
        )
        dummyData.categories[indexCategory] = newCategory
        dummyData.emitCounterUpdate()
        dummyData.emitCategoryUpdate()
    }

    override suspend fun deleteCounter(counter: Counter) {
        dummyData.counters.removeIf { it.id == counter.id }
        Log.i("FakeCounterRepository", "Counter deleted: $counter")
        Log.i("Category id : ", "${counter.categoryId}")
        val indexCategory = dummyData.categories.indexOfFirst { it.id == counter.categoryId }
        val category = dummyData.categories[indexCategory]
        val newCategory = category.copy(
            countersCount = category.countersCount - 1
        )
        dummyData.categories[indexCategory] = newCategory
        dummyData.emitCounterUpdate()
        dummyData.emitCategoryUpdate()
    }

    override fun getCountersWithCategories(): Flow<List<CounterWithCategory>> {
        return countersFlow.map {
            it.filter {
                !it.isSystem
            }.sortedByDescending { it.orderAnchorAt }
                .map { counter ->
                val category = dummyData.categories.find { category ->
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

            val categoriesMap = dummyData.categories.filter { !it.isSystem }
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

    override fun getSystemCounters(): Flow<List<Counter>> {
        return countersFlow.map { list ->
            list.filter { it.isSystem }
        }
    }

    override suspend fun incrementSystemCounter(counterKey: String) {
        dummyData.counters.indexOfFirst {
            it.kay == counterKey
        }.let { index ->
            if (index != -1) {
                val counter = dummyData.counters[index]
                val newCounter = counter.copy(
                    currentCount = counter.currentCount + 1
                )
                dummyData.counters[index] = newCounter
                dummyData.emitCounterUpdate()
            }
        }
    }

    override suspend fun updateSystemCounter(counterKey: String, count: Int) {
        dummyData.counters.indexOfFirst {
            it.kay == counterKey
        }.let { index ->
            if (index != -1) {
                val counter = dummyData.counters[index]
                val newCounter = counter.copy(
                    currentCount = count
                )
                dummyData.counters[index] = newCounter
                dummyData.emitCounterUpdate()
            }
        }
    }

    override suspend fun deleteAllCounters() {
        dummyData.counters.clear();
        dummyData.emitCounterUpdate()
    }

    override fun getCounter(id: String): Flow<Counter?> {
        return countersFlow.map { list ->
            list.firstOrNull { it.id == id }
        }
    }


    override fun getAllCounters(): Flow<List<Counter>> =
        countersFlow

}
