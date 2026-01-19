package io.droidevs.counterapp.ui.adapter.models

import io.droidevs.counterapp.ui.adapter.base.DiffableItem
import io.droidevs.counterapp.ui.models.CounterWithCategoryUiModel

data class CounterWithCategoryItem(
    val model: CounterWithCategoryUiModel
) : DiffableItem {
    override val diffId: Any = model.counter.id
    override val diffContent: Any = model
}

