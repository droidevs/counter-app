package io.droidevs.counterapp.domain.model

import java.time.Instant

data class Category(
    val id: String,
    val name: String,
    val color: CategoryColor,
    val countersCount: Int,
    val isSystem: Boolean = false,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
) {
    companion object {
        fun default() = Category(
            id = "dummy_id",
            name = "Dummy Category",
            color = CategoryColor.default(),
            countersCount = 0
        )
    }
}
