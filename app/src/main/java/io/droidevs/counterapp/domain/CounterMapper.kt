package io.droidevs.counterapp.domain

import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.models.CounterUiModel


fun Counter.toUiModel(
    formatter: DateFormatter
): CounterUiModel {
    val wasUserUpdated = lastUpdatedAt != null && lastUpdatedAt!!.isAfter(createdAt)

    return CounterUiModel(
        id = id,
        name = name,
        currentCount = currentCount,
        categoryId = categoryId,
        canIncrease = canIncrease,
        canDecrease = canDecrease,
        isSystem = isSystem,
        systemKey = systemKey,
        createdTime = formatter.format(createdAt),
        editedTime = lastUpdatedAt?.let { formatter.format(it) },
        wasUserUpdated = wasUserUpdated
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
        isSystem = isSystem,
        systemKey = systemKey
    )
}