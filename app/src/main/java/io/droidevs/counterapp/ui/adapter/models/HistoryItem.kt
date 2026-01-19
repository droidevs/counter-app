package io.droidevs.counterapp.ui.adapter.models

import io.droidevs.counterapp.ui.adapter.base.DiffableItem
import io.droidevs.counterapp.ui.models.HistoryUiModel

data class HistoryItem(
    val model: HistoryUiModel
) : DiffableItem {
    override val diffId: Any = model.id
    override val diffContent: Any = model
}

