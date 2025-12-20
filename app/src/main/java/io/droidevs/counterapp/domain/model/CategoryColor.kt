package io.droidevs.counterapp.domain.model

data class CategoryColor(
    val colorInt: Int
) {
    companion object {
        fun default() = CategoryColor(colorInt = 0)
    }
}
