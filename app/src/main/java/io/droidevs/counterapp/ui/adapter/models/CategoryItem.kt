package io.droidevs.counterapp.ui.adapter.models

import io.droidevs.counterapp.ui.adapter.base.DiffableItem
import io.droidevs.counterapp.ui.models.CategoryUiModel

data class CategoryItem(
    val model: CategoryUiModel
) : DiffableItem {
    override val diffId: Any = model.id
    override val diffContent: Any = model
}

