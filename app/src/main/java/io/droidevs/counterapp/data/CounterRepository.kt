package io.droidevs.counterapp.data

import android.util.Log
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.repository.CounterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CounterRepositoryImpl(
    private var dao: CounterDao
) : CounterRepository {

    override fun getAllCounters(): Flow<List<Counter>> {
        return dao.getAll().map { counters ->
            counters.map { it.toDomain() }
        }
    }

    override fun getLastEdited(limit: Int): Flow<List<Counter>> {
        return dao.getLastEdited(5).map { counters ->
            counters.map { it.toDomain() }
        }
    }

    override fun getTotalCounters() : Flow<Int> {
        Log.i("CounterRepository", "Getting total counters")
        return dao.getTotalCounters()
    }

    override suspend fun saveCounter(counter : Counter) {
        val n = dao.update(counter.toEntity())
        Log.i("CounterRepository", "Updated $n counters")
    }

}
