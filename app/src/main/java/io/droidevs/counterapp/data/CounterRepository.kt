package io.droidevs.counterapp.data

import android.util.Log
import io.droidevs.counterapp.model.Counter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

import java.time.Instant

class CounterRepository(
    private var dao: CounterDao
) {

    fun getAllCounters(): Flow<List<Counter>> {
        return dao.getLastEdited(5).map { counters ->
            counters.map { it.toDomain() }
        }
    }

    fun getTotalCounters() : Flow<Int> {
        Log.i("CounterRepository", "Getting total counters")
        return dao.getTotalCounters()
    }

    suspend fun saveCounter(counter : Counter) {
        val n = dao.update(counter.toEntity())
        Log.i("CounterRepository", "Updated $n counters")
    }

}
