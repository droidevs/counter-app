package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.CategoryViewViewModel

@Deprecated("Use Hilt injection with @HiltViewModel instead")
class CategoryViewViewModelFactory(
    private val savedStateHandle: SavedStateHandle,
    private val categoryUseCases: CategoryUseCases,
    private val counterUseCases: CounterUseCases,
    private val dateFormatter: DateFormatter,
    private val uiMessageDispatcher: UiMessageDispatcher
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoryViewViewModel(
                savedStateHandle = savedStateHandle,
                categoryUseCases = categoryUseCases,
                dateFormatter = dateFormatter,
                uiMessageDispatcher = uiMessageDispatcher,
                counterUseCases = counterUseCases
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
