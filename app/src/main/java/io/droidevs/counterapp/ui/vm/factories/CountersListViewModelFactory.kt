package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.CountersListViewModel

@Deprecated("Use Hilt injection with @HiltViewModel instead")
class CountersListViewModelFactory(
    private val counterUseCases: CounterUseCases,
    private val dateFormatter: DateFormatter,
    private val uiMessageDispatcher: UiMessageDispatcher
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountersListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CountersListViewModel(
                counterUseCases = counterUseCases,
                dateFormatter = dateFormatter,
                uiMessageDispatcher = uiMessageDispatcher
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
