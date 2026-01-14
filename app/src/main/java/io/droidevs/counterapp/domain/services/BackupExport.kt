package io.droidevs.counterapp.domain.services

data class BackupExport(
    val counters: List<CounterExport>,
    val categories: List<CategoryExport>
)
