package io.droidevs.counterapp.data.fake

import io.droidevs.counterapp.data.toDomain
import io.droidevs.counterapp.data.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.repository.CounterRepository

class FakeCounterRepository : CounterRepository {

    // Internal mutable list simulating the database
    private val countersData = DummyData.getCounters().toMutableList()

    // MutableStateFlow to simulate reactive updates
    private val _countersFlow = MutableStateFlow(countersData.toList())

    // ------------------- Counter Operations -------------------

    override fun getLastEdited(limit: Int): Flow<List<Counter>> {
        return _countersFlow.map { list ->
            list.sortedByDescending { it.lastUpdatedAt }
                .take(limit)
                .map { it.toDomain() }
        }
    }

    override fun getTotalCounters(): Flow<Int> {
        return _countersFlow.map { it.size }
    }


    suspend fun insertAll(newCounters: List<Counter>) {
        countersData.addAll(newCounters.map { it.toEntity() })
        _countersFlow.value = countersData.toList()
    }

    override suspend fun saveCounter(counter: Counter) {
        val index = countersData.indexOfFirst { it.id == counter.id }
        if (index != -1) {
            countersData[index] = counter.toEntity()
            _countersFlow.value = countersData.toList()
        }
    }

    override fun getAllCounters(): Flow<List<Counter>> =
        _countersFlow.map { counters ->
            counters.map { it.toDomain() }
        }
}
