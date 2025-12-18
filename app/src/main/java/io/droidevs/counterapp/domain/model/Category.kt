package io.droidevs.counterapp.domain.model

data class Category(
    val id: String,
    val name: String,
    val countersCount: Int
) {
    companion object {
        fun default() = Category(
            id = "",
            name = "",
            countersCount = 0
        )
    }
}
