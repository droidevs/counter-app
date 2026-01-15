package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.CreateCounterViewModel

@Deprecated("Use Hilt injection with @HiltViewModel instead")
class CreateCounterViewModelFactory(
    private val counterUseCases: CounterUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val savedStateHandle: SavedStateHandle,
    private val dateFormatter: DateFormatter,
    private val uiMessageDispatcher: UiMessageDispatcher
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateCounterViewModel::class.java)) {
            return CreateCounterViewModel(
                savedStateHandle = savedStateHandle,
                counterUseCases = counterUseCases,
                categoryUseCases = categoryUseCases,
                dateFormatter = dateFormatter,
                uiMessageDispatcher = uiMessageDispatcher
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}