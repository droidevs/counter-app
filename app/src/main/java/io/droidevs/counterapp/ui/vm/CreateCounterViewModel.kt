package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.domain.usecases.requests.CreateCounterRequest
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.domain.toUiModel
import io.droidevs.counterapp.ui.models.CounterUiModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CreateCounterViewModel(
    private val counterUseCases: CounterUseCases,
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {


    val categories = categoryUseCases.getAllCategories()
        .map { categories ->
            categories.map { it.toUiModel() }
        }


    fun saveCounter(
        counter: CounterUiModel,
        onCounterSaved: () -> Unit
    ) {
        viewModelScope.launch {
            counterUseCases.createCounter(CreateCounterRequest.of(counter.toDomain()))
            onCounterSaved()
        }
    }
}