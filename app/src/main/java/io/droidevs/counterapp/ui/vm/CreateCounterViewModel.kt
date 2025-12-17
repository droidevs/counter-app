package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.ui.models.CounterSnapshot
import io.droidevs.counterapp.ui.toDomain
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CreateCounterViewModel(
    val repository : CounterRepository,
    val categoryRepository: CategoryRepository
) : ViewModel() {


    val categories = categoryRepository.allCategories()
        .map { categories ->
            categories.map { it.toUiModel() }
        }


    fun saveCounter(
        counter: CounterSnapshot,
        onCounterSaved: () -> Unit
    ) {
        viewModelScope.launch {
            repository.createCounter(counter.toDomain())
        }
        onCounterSaved()
    }
}