package io.droidevs.counterapp.initializer

import io.droidevs.counterapp.data.DefaultData
import io.droidevs.counterapp.domain.repository.DataInitializer
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError
import io.droidevs.counterapp.domain.result.runCatchingResult
import io.droidevs.counterapp.domain.system.SystemCategory
import io.droidevs.counterapp.repository.DummyData

class FakeDataInitializer(private val dummyData: DummyData) : DataInitializer {
    override suspend fun init(): Result<Unit, DatabaseError> = runCatchingResult(
        errorTransform = { DatabaseError.UnknownError(it) }
    ) {
        dummyData.categories.addAll(
            DefaultData.buildCategories(
                existing = emptyMap()
            )
        )
        dummyData.emitCategoryUpdate()

        val categoryIdMap: Map<SystemCategory, String> = dummyData.categories
            .mapNotNull { category ->
                val key = category.kay?.trim().orEmpty()
                if (key.isBlank()) return@mapNotNull null

                val systemCategory = runCatching { SystemCategory.valueOf(key) }.getOrNull()
                    ?: return@mapNotNull null

                systemCategory to category.id
            }
            .toMap()

        dummyData.counters.addAll(
            DefaultData.buildCounters(
                existing = emptyMap(),
                categoryIdMap = categoryIdMap
            )
        )
        dummyData.emitCounterUpdate()
    }
}