package io.droidevs.counterapp.domain.model

data class Backup(
    val counters: List<Counter>,
    val categories: List<Category>
)
