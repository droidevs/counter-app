package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.ui.vm.CountersListViewModel

@Deprecated("Use Hilt injection with @HiltViewModel instead")
class CountersListViewModelFactory(
    private val counterUseCases: CounterUseCases
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountersListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CountersListViewModel(counterUseCases) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
