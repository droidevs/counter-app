package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.ui.date.DateFormatter
import io.droidevs.counterapp.ui.vm.CategoryViewViewModel

@Deprecated("Use Hilt injection with @HiltViewModel instead")
class CategoryViewViewModelFactory(
    private val categoryId: String,
    private val savedStateHandle: SavedStateHandle,
    private val categoryUseCases: CategoryUseCases,
    private val dateFormatter: DateFormatter
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoryViewViewModel(
                //categoryId = categoryId,
                savedStateHandle = savedStateHandle,
                categoryUseCases = categoryUseCases,
                dateFormatter = dateFormatter
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
