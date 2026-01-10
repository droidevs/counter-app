package io.droidevs.counterapp.di

import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases

/**
 * Simple holder for grouped use cases to be accessible from the Application.
 */
data class AppUseCases(
    val counterUseCases: CounterUseCases,
    val categoryUseCases: CategoryUseCases
)

