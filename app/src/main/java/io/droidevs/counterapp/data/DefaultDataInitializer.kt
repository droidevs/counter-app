package io.droidevs.counterapp.data

import io.droidevs.counterapp.data.dao.CategoryDao
import io.droidevs.counterapp.data.dao.CounterDao
import io.droidevs.counterapp.domain.system.SystemCategory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

object DefaultDataInitializer {

    suspend fun init(
        categoryDao: CategoryDao,
        counterDao: CounterDao
    ) {

        val existingCategories = categoryDao.getSystemCategories().firstOrNull()
            ?.associateBy { it.key!! }

        val existingCounters = counterDao.getAllSystem().firstOrNull()
            ?.associateBy { it.key!! }

        val categories = DefaultData.buildCategories(existingCategories?: emptyMap())

        categories.forEach { categoryDao.insert(it) }

        val categoryIdMap = categories.associate {
            SystemCategory.valueOf(it.key!!) to it.id
        }

        val counters = DefaultData.buildCounters(
            existing = existingCounters?: emptyMap(),
            categoryIdMap = categoryIdMap
        )

        counters.forEach { counterDao.insert(it) }
    }
}
