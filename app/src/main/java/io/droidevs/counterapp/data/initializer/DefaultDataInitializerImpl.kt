package io.droidevs.counterapp.data.initializer

import io.droidevs.counterapp.data.DefaultData
import io.droidevs.counterapp.data.dao.CategoryDao
import io.droidevs.counterapp.data.dao.CounterDao
import io.droidevs.counterapp.data.repository.exceptions.runCatchingDatabaseResult
import io.droidevs.counterapp.domain.repository.DataInitializer
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import io.droidevs.counterapp.domain.system.SystemCategory
import kotlinx.coroutines.flow.firstOrNull

class DefaultDataInitializerImpl(
    private val categoryDao: CategoryDao,
    private val counterDao: CounterDao
) : DataInitializer {

    override suspend fun init(): Result<Unit, DatabaseError> = runCatchingDatabaseResult {
        val existingCategories = categoryDao.getSystemCategories().firstOrNull()
            ?.associateBy { it.kay!! }

        val existingCounters = counterDao.getAllSystem().firstOrNull()
            ?.associateBy { it.kay!! }

        val categories = DefaultData.buildCategories(existingCategories ?: emptyMap())

        categories.forEach { categoryDao.insert(it) }

        val categoryIdMap = categories.associate {
            SystemCategory.valueOf(it.kay!!) to it.id
        }

        val counters = DefaultData.buildCounters(
            existing = existingCounters ?: emptyMap(),
            categoryIdMap = categoryIdMap
        )

        counters.forEach { counterDao.insert(it) }
    }
}
