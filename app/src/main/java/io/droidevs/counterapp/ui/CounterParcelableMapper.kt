package io.droidevs.counterapp.ui

import io.droidevs.counterapp.ui.models.CounterSnapshot
import java.time.Instant

// Map CounterSnapshot -> Parcelable
fun CounterSnapshot.toParcelable(): CounterSnapshotParcelable = CounterSnapshotParcelable(
    id = id,
    name = name,
    currentCount = currentCount,
    createdAtMillis = createdAt.toEpochMilli(),
    lastUpdatedAtMillis = lastUpdatedAt.toEpochMilli(),
    canIncrease = canIncrease,
    canDecrease = canDecrease
)

// Map Parcelable -> CounterSnapshot
fun CounterSnapshotParcelable.toUiModel(): CounterSnapshot = CounterSnapshot(
    id = id,
    name = name,
    currentCount = currentCount,
    createdAt = Instant.ofEpochMilli(createdAtMillis),
    lastUpdatedAt = Instant.ofEpochMilli(lastUpdatedAtMillis),
    canIncrease = canIncrease,
    canDecrease = canDecrease
)
