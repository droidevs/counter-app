package io.droidevs.counterapp.data

import io.droidevs.counterapp.model.Counter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import java.time.Instant

class CounterRepository(var dao: CounterDao) {

    fun getAllCounters(): Flow<List<Counter>> {
        return dao.getLastEdited(5).map { counters ->
            counters.map { it.toDomain() }
        }
    }

    fun getTotalCounters() : Flow<Int> {
        return dao.getTotalCounters()
    }

}
