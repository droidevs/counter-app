package io.droidevs.counterapp.data.repository

import io.droidevs.bmicalc.data.db.exceptions.DatabaseException
import io.droidevs.counterapp.data.dao.CategoryDao
import io.droidevs.counterapp.data.dao.CounterDao
import io.droidevs.counterapp.data.repository.exceptions.flowRunCatchingDatabase
import io.droidevs.counterapp.data.repository.exceptions.runCatchingDatabaseResult
import io.droidevs.counterapp.data.toDomain
import io.droidevs.counterapp.data.toEntity
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.domain.model.CounterWithCategory
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import io.droidevs.counterapp.domain.toDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class CounterRepositoryImpl(
    private var dao: CounterDao,
    private var categoryDao: CategoryDao
) : CounterRepository {
    override fun getCounter(id: String): Flow<Result<Counter, DatabaseError>> = flowRunCatchingDatabase {
        dao.getCounter(id)
            .map { it?.toDomain() ?: throw DatabaseException.NoElementFound() }
    }

    /**
     * Deprecated: Use getAllCountersPaged instead.
     */
    @Deprecated("Use getAllCountersPaged(pageNumber, pageSize) for pagination support.")
    override fun getAllCounters(): Flow<Result<List<Counter>, DatabaseError>> = flowRunCatchingDatabase {
        dao.getAll().map { counters ->
            counters.map { it.toDomain() }
        }
    }

    /**
     * New paginated method for counters.
     */
    override fun getAllCountersPaged(pageNumber: Int, pageSize: Int): Flow<Result<List<Counter>, DatabaseError>> = flowRunCatchingDatabase {
        dao.getUserCountersPaged(pageNumber, pageSize).map { counters ->
            counters.map { it.toDomain() }
        }
    }

    override fun getLastEdited(limit: Int): Flow<Result<List<Counter>, DatabaseError>> = flowRunCatchingDatabase {
        dao.getCounters(limit).map { counters ->
            counters.map { it.toDomain() }
        }
    }

    override fun getTotalCounters(): Flow<Result<Int, DatabaseError>> = flowRunCatchingDatabase {
        dao.getTotalCounters()
    }

    override suspend fun saveCounter(counter: Counter): Result<Unit, DatabaseError> = runCatchingDatabaseResult {
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

    override suspend fun createCounter(counter: Counter): Result<Unit, DatabaseError> = runCatchingDatabaseResult {
        dao.upsert(counter.toEntity())
        counter.categoryId?.let { categoryId ->
            categoryDao.incrementCounterCount(categoryId)
        }
    }

    override suspend fun deleteCounter(counter: Counter): Result<Unit, DatabaseError> = runCatchingDatabaseResult {
        dao.delete(counter.toEntity())
        counter.categoryId?.let { categoryId ->
            categoryDao.decrementCounterCount(categoryId)
        }
    }

    override fun getCountersWithCategories(): Flow<Result<List<CounterWithCategory>, DatabaseError>> = flowRunCatchingDatabase {
        dao.getCountersWithCategories().map { data ->
            data.map { it.toDomainModel() }
        }
    }

    override fun getCountersWithCategories(limit: Int): Flow<Result<List<CounterWithCategory>, DatabaseError>> =
        flowRunCatchingDatabase {
            dao.getCountersWithCategories(limit).map { data ->
                data.map { it.toDomainModel() }
            }
        }

    override fun getLastEditedWithCategory(limit: Int): Flow<Result<List<CounterWithCategory>, DatabaseError>> = flowRunCatchingDatabase {
        dao.getLastEditedWithCategories(limit).map { data ->
            data.map { it.toDomainModel() }
        }
    }

    /**
     * Deprecated: Use getSystemCountersPaged instead.
     */
    @Deprecated("Use getSystemCountersPaged(pageNumber, pageSize) for pagination support.")
    override fun getSystemCounters(): Flow<Result<List<Counter>, DatabaseError>> = flowRunCatchingDatabase {
        dao.getAllSystem().map { counters ->
            counters.map { it.toDomain() }
        }
    }

    /**
     * New paginated method for system counters.
     */
    override fun getSystemCountersPaged(pageNumber: Int, pageSize: Int): Flow<Result<List<Counter>, DatabaseError>> = flowRunCatchingDatabase {
        dao.getSystemCountersPaged(pageNumber, pageSize).map { counters ->
            counters.map { it.toDomain() }
        }
    }

    override suspend fun incrementSystemCounter(counterKey: String): Result<Unit, DatabaseError> = runCatchingDatabaseResult {
        dao.incrementSystemCounter(counterKey)
    }

    override suspend fun updateSystemCounter(counterKey: String, count: Int): Result<Unit, DatabaseError> = runCatchingDatabaseResult {
        dao.updateSystemCounter(counterKey, count)
    }

    override suspend fun deleteAllCounters(): Result<Unit, DatabaseError> = runCatchingDatabaseResult {
        dao.deleteAllUserCounters()
        categoryDao.resetAllUserCategoryCounts()
    }

    override suspend fun exportCounters(): Result<List<Counter>, DatabaseError> = runCatchingDatabaseResult {
        dao.getAllUserCounters().first().map { it.toDomain() }
    }

    override suspend fun importCounters(counters: List<Counter>): Result<Unit, DatabaseError> = runCatchingDatabaseResult {
        dao.upsertAll(counters.map { it.toEntity() })
        categoryDao.recalculateAllCategoryCounts()
    }
}
