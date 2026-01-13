package io.droidevs.counterapp.initializer

import io.droidevs.counterapp.data.DefaultData
import io.droidevs.counterapp.domain.repository.DataInitializer
import io.droidevs.counterapp.domain.system.SystemCategory
import io.droidevs.counterapp.repository.DummyData

class FakeDataInitializer(private val dummyData: DummyData) : DataInitializer {
    override suspend fun init() {
        dummyData.categories.addAll(
            DefaultData.buildCategories(
                existing = emptyMap()
            )
        )
        dummyData.emitCategoryUpdate()

        dummyData.counters.addAll(
            DefaultData.buildCounters(
                existing = emptyMap(),
                // generate the list from categories dummy data that has kay != null
                categoryIdMap = dummyData.categories
                    .filter { it.kay != null }                    // Skip null kay
                    .associate { category ->                       // Build the map
                        SystemCategory.valueOf(category.kay!!) to category.id
                    }
            )
        )
        dummyData.emitCounterUpdate()
    }
}