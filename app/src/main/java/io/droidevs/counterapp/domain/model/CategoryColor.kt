package io.droidevs.counterapp.domain.model

data class CategoryColor(
    val colorInt: Int
) {
    companion object {

        fun of(colorInt: Int) = CategoryColor(colorInt)
        fun default() = CategoryColor(colorInt = 0)
    }
}
