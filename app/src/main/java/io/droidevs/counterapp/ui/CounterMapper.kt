package io.droidevs.counterapp.ui

import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.ui.models.CounterSnapshot


fun Counter.toSnapshot(): CounterSnapshot {
    return CounterSnapshot(
        id = id,
        name = name,
        currentCount = currentCount,
        createdAt = createdAt,
        lastUpdatedAt = lastUpdatedAt,
        canIncrease = canIncrease,
        canDecrease = canDecrease
    )
}

fun CounterSnapshot.toDomain(): Counter {
    return Counter(
        id = id,
        name = name,
        currentCount = currentCount,
        canIncrease = canIncrease,
        canDecrease = canDecrease,
        createdAt = createdAt,
        lastUpdatedAt = lastUpdatedAt
    )
}

