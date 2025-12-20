package io.droidevs.counterapp.data

import io.droidevs.counterapp.data.entities.CounterEntity
import io.droidevs.counterapp.domain.model.Counter


fun CounterEntity.toDomain(): Counter {
    return Counter(
        id = id,
        name = name,
        currentCount = currentCount,
        createdAt = createdAt,
        categoryId = categoryId,
        canIncrease = canIncrement,
        canDecrease = canDecrement,
        isSystem = isSystem
    )
}

fun Counter.toEntity(): CounterEntity {
    return CounterEntity(
        id = id,
        name = name,
        currentCount = currentCount,
        categoryId = categoryId,
        createdAt = createdAt,
        lastUpdatedAt = lastUpdatedAt,
        canIncrement = canIncrease,
        canDecrement = canDecrease,
        isSystem = isSystem
    )
}