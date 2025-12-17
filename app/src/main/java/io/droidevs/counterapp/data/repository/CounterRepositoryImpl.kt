package io.droidevs.counterapp.data.repository

import android.util.Log
import io.droidevs.counterapp.data.dao.CategoryDao
import io.droidevs.counterapp.data.dao.CounterDao
import io.droidevs.counterapp.data.toDomain
import io.droidevs.counterapp.data.toEntity
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.repository.CounterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class CounterRepositoryImpl(
    private var dao: CounterDao,
    private var categoryDao: CategoryDao
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

    override suspend fun createCounter(counter: Counter) {
        dao.insert(counter.toEntity())
        val category = categoryDao.getCategory(counter.categoryId.toString()).first()
        category.copy(
            countersCount = category.countersCount + 1
        )
        categoryDao.updateCategory(category)
    }
}