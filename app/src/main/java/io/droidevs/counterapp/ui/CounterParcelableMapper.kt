package io.droidevs.counterapp.ui

import io.droidevs.counterapp.ui.models.CounterUiModel
import java.time.Instant

// Map CounterUiModel -> Parcelable
fun CounterUiModel.toParcelable(): CounterSnapshotParcelable = CounterSnapshotParcelable(
    id = id,
    name = name,
    currentCount = currentCount,
    categoryId = categoryId.toString(),
    createdAtMillis = createdAt.toEpochMilli(),
    lastUpdatedAtMillis = lastUpdatedAt.toEpochMilli(),
    canIncrease = canIncrease,
    canDecrease = canDecrease,
    orderAnchorAt = orderAnchorAt.toEpochMilli()
)

// Map Parcelable -> CounterUiModel
fun CounterSnapshotParcelable.toUiModel(): CounterUiModel = CounterUiModel(
    id = id,
    name = name,
    currentCount = currentCount,
    categoryId = categoryId,
    createdAt = Instant.ofEpochMilli(createdAtMillis),
    lastUpdatedAt = Instant.ofEpochMilli(lastUpdatedAtMillis),
    canIncrease = canIncrease,
    canDecrease = canDecrease,
    orderAnchorAt = Instant.ofEpochMilli(orderAnchorAt)
)
