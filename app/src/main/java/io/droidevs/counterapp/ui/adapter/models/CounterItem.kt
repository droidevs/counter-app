package io.droidevs.counterapp.ui.adapter.models

import io.droidevs.counterapp.ui.adapter.base.DiffableItem
import io.droidevs.counterapp.ui.models.CounterUiModel

data class CounterItem(
    val model: CounterUiModel
) : DiffableItem {
    override val diffId: Any = model.id
    override val diffContent: Any = model
}

