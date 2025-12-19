package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.toSnapshot
import io.droidevs.counterapp.domain.toUiModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class HomeViewModel(
    val counterRepository: CounterRepository,
    val categoryRepository: CategoryRepository
) : ViewModel() {


    val countersSnapshots = counterRepository.getLastEditedWithCategory(6)
        .onStart { emit(emptyList()) }
        .map { counters ->
            counters.map {
                it.toUiModel()
            }
        }

    val countersNumber = counterRepository.getTotalCounters()
        .onStart { emit(0) }

    val categoriesCount = categoryRepository.getTotalCategoriesCount()
        .onStart { emit(0) }


    val categories = categoryRepository.topCategories(3)
        .onStart { emit(emptyList()) }
        .map { categories ->
            categories.map {
                it.toUiModel()
            }
        }

}