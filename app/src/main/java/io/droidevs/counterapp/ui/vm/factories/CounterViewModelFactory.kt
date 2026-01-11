package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.vm.CounterViewViewModel

@Deprecated("Use Hilt injection with @HiltViewModel instead")
class CounterViewModelFactory(
    //val counter : CounterUiModel,
    val savedStateHandle: SavedStateHandle,
    val counterUseCases: CounterUseCases
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CounterViewViewModel::class.java))
            return CounterViewViewModel(
                //initialCounter = counter,
                savedStateHandle = savedStateHandle,
                counterUseCases = counterUseCases
            ) as T
        else
            throw IllegalArgumentException("Unknown ViewModel class")
    }
}