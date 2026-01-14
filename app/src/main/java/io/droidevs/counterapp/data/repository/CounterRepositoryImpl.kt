package io.droidevs.counterapp.data.repository

import android.util.Log
import io.droidevs.counterapp.data.dao.CategoryDao
import io.droidevs.counterapp.data.dao.CounterDao
import io.droidevs.counterapp.data.toDomain
import io.droidevs.counterapp.data.toEntity
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.model.CounterWithCategory
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.toDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class CounterRepositoryImpl(
    private var dao: CounterDao,
    private var categoryDao: CategoryDao
) : CounterRepository {
    override fun getCounter(id: String): Flow<Counter?> {
        return dao.getCounter(id)
            .map { it?.toDomain() }
    }

    override fun getAllCounters(): Flow<List<Counter>> {
        return dao.getAll().map { counters ->
            counters.map { it.toDomain() }
        }
    }

    override fun getLastEdited(limit: Int): Flow<List<Counter>> {
        return dao.getCounters(limit).map { counters ->
            counters.map { it.toDomain() }
        }
    }

    override fun getTotalCounters() : Flow<Int> {
        return dao.getTotalCounters()
    }

    override suspend fun saveCounter(counter : Counter) {
        val oldCounter = dao.getCounter(counter.id).first()
        dao.upsert(counter.toEntity())

        if (oldCounter?.categoryId != counter.categoryId) {
            oldCounter?.categoryId?.let { oldCatId ->
                categoryDao.decrementCounterCount(oldCatId)
            }
            counter.categoryId?.let { newCatId ->
                categoryDao.incrementCounterCount(newCatId)
            }
        }
    }

    override suspend fun createCounter(counter: Counter) {
        dao.upsert(counter.toEntity())
        counter.categoryId?.let { categoryId ->
            categoryDao.incrementCounterCount(categoryId)
        }
    }

    override suspend fun deleteCounter(counter: Counter) {
        dao.delete(counter.toEntity())
        counter.categoryId?.let { categoryId ->
            categoryDao.decrementCounterCount(categoryId)
        }
    }

    override fun getCountersWithCategories(): Flow<List<CounterWithCategory>> {
        return dao.getCountersWithCategories().map { data ->
            data.map { it.toDomainModel() }
        }
    }

    override fun getLastEditedWithCategory(limit: Int): Flow<List<CounterWithCategory>> {
        return dao.getCountersWithCategories(limit).map { data ->
            data.map { it.toDomainModel() }
        }
    }

    override fun getSystemCounters(): Flow<List<Counter>> {
        return dao.getAllSystem().map { counters ->
            counters.map { it.toDomain() }
        }
    }

    override suspend fun incrementSystemCounter(counterKey: String) {
        dao.incrementSystemCounter(counterKey)
    }

    override suspend fun updateSystemCounter(counterKey: String, count: Int) {
        dao.updateSystemCounter(counterKey, count)
    }

    override suspend fun deleteAllCounters() {
        dao.deleteAllUserCounters()
        categoryDao.resetAllUserCategoryCounts()
    }

    override suspend fun exportCounters(): List<Counter> {
        return dao.getAllUserCounters().first().map { it.toDomain() }
    }

    override suspend fun importCounters(counters: List<Counter>) {
        dao.upsertAll(counters.map { it.toEntity() })
        categoryDao.recalculateAllCategoryCounts()
    }
}