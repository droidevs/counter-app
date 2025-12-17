package io.droidevs.counterapp.domain.model

data class CategoryWithCounters(
    val category: Category,
    val counters: List<Counter>
)