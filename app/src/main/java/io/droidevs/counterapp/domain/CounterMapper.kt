package io.droidevs.counterapp.domain

import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.ui.CounterSnapshot


fun Counter.toSnapshot(): CounterSnapshot {
    return CounterSnapshot(
        id = id,
        name = name,
        currentCount = currentCount,
        lastUpdatedAt = lastUpdatedAt,
        createdAt =  createdAt,
        canDecrease = canDecrease,
        canIncrease = canIncrease
    )
}