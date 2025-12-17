package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.counterapp.domain.repository.CategoryRepository
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.ui.models.CategoryUiModel
import kotlinx.coroutines.launch

class CreateCategoryViewModel(
    private val repository: CategoryRepository
) : ViewModel() {


    fun saveCategory(
        category: CategoryUiModel,
        onSuccess: (() -> Unit)? = null,
    ) {

        viewModelScope.launch {
            repository.createCategory(category.toDomain())
        }

        onSuccess?.invoke()
    }
}