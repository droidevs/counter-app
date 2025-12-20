package io.droidevs.counterapp.domain.model

data class Category(
    val id: String,
    val name: String,
    val color: CategoryColor,
    val countersCount: Int
) {
    companion object {
        fun default() = Category(
            id = "",
            name = "",
            color = CategoryColor.default(),
            countersCount = 0
        )
    }
}
