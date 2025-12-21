package io.droidevs.counterapp.domain

import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.ui.models.CounterUiModel


fun Counter.toSnapshot(): CounterUiModel {
    return CounterUiModel(
        id = id,
        name = name,
        currentCount = currentCount,
        categoryId = categoryId,
        createdAt = createdAt,
        lastUpdatedAt = lastUpdatedAt,
        canIncrease = canIncrease,
        canDecrease = canDecrease,
        orderAnchorAt = orderAnchorAt
    )
}

fun CounterUiModel.toDomain(): Counter {
    return Counter(
        id = id,
        name = name,
        currentCount = currentCount,
        canIncrease = canIncrease,
        canDecrease = canDecrease,
        categoryId = categoryId,
        createdAt = createdAt,
        lastUpdatedAt = lastUpdatedAt,
        orderAnchorAt = orderAnchorAt
    )
}