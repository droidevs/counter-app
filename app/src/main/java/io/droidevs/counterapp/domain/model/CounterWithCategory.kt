package io.droidevs.counterapp.domain.model



data class CounterWithCategory(
    val counter: Counter,
    val category: Category?
)