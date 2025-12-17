package io.droidevs.counterapp.data.fake

import io.droidevs.counterapp.data.toDomain
import io.droidevs.counterapp.data.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.repository.CounterRepository

class FakeCounterRepository(
    val dummyData: DummyData
) : CounterRepository {

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

    override fun getAllCounters(): Flow<List<Counter>> =
        DummyData.countersFlow.map { counters ->
            counters.map { it.toDomain() }
        }

}
