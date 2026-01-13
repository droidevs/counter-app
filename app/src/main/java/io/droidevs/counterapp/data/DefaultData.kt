package io.droidevs.counterapp.data

import io.droidevs.counterapp.data.entities.CategoryEntity
import io.droidevs.counterapp.data.entities.CounterEntity
import io.droidevs.counterapp.domain.system.SystemCategory
import io.droidevs.counterapp.domain.system.SystemCounterType
import java.time.Instant
import java.util.UUID

object DefaultData {

    /**
     * Builds system categories.
     * If a category already exists, its ID is reused.
     */
    fun buildCategories(
        existing: Map<String, CategoryEntity> // kay -> entity
    ): List<CategoryEntity> =
        SystemCategory.entries.map { category ->
            existing[category.name] ?: CategoryEntity(
                id = UUID.randomUUID().toString(),
                kay = category.name,
                name = category.displayName,
                color = category.color,
                isSystem = true,
                countersCount = SystemCounterType.entries.count { it.category == category }
            )
        }

    fun buildCounters(
        existing: Map<String, CounterEntity>,           // kay -> entity
        categoryIdMap: Map<SystemCategory, String>
    ): List<CounterEntity> =
        SystemCounterType.entries
            .filter { it.key !in existing.keys }
            .map { counter ->
                CounterEntity(
                    id = UUID.randomUUID().toString(),
                    kay = counter.key,
                    name = counter.displayName,
                    categoryId = categoryIdMap[counter.category]!!,
                    currentCount = counter.defaultValue,
                    isSystem = true,
                    canDecrement = true,
                    canIncrement = true,
                    createdAt = Instant.now(),
                    lastUpdatedAt = Instant.now(),
                    orderAnchorAt = Instant.now()
                )
            }
}
