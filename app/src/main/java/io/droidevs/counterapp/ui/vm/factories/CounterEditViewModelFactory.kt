package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.vm.CounterEditViewModel

@Deprecated("Use Hilt injection with @HiltViewModel instead")
class CounterEditViewModelFactory(
    private val initialCounter: CounterUiModel,
    private val savedStateHandle: SavedStateHandle,
    private val counterUseCases: CounterUseCases
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CounterEditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CounterEditViewModel(
                //initialCounter = initialCounter,
                savedStateHandle = savedStateHandle,
                counterUseCases = counterUseCases
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
