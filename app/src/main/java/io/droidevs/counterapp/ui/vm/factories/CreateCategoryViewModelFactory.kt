package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.vm.CreateCategoryViewModel

@Deprecated("Use Hilt injection with @HiltViewModel instead")
class CreateCategoryViewModelFactory(
    private val categoryUseCases: CategoryUseCases,
    private val uiMessageDispatcher: UiMessageDispatcher
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateCategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateCategoryViewModel(
                categoryUseCases = categoryUseCases,
                uiMessageDispatcher = uiMessageDispatcher
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}