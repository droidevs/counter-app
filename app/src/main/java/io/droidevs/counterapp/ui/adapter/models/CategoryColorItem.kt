package io.droidevs.counterapp.ui.adapter.models

import io.droidevs.counterapp.domain.model.CategoryColor
import io.droidevs.counterapp.ui.adapter.base.DiffableItem

data class CategoryColorItem(
    val model: CategoryColor
) : DiffableItem {
    override val diffId: Any = model.colorInt
    override val diffContent: Any = model.colorInt
}

