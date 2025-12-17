package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import io.droidevs.counterapp.data.CategoryRepository
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.toSnapshot
import io.droidevs.counterapp.domain.toUiModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class HomeViewModel(
    val counterRepository: CounterRepository,
    val categoryRepository: CategoryRepository
) : ViewModel() {


    val countersSnapshots = counterRepository.getAllCounters()
        .onStart { emit(emptyList()) }
        .map { counters ->
            counters.map {
                it.toSnapshot()
            }
        }

    val countersNumber = counterRepository.getTotalCounters()
        .onStart { emit(0) }


    val categories = categoryRepository.topCategories()
        .onStart { emit(emptyList()) }
        .map { categories ->
            categories.map {
                it.toUiModel()
            }
        }

}