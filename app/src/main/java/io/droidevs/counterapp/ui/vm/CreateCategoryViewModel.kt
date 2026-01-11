package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.domain.usecases.category.requests.CreateCategoryRequest
import io.droidevs.counterapp.domain.toDomain
import io.droidevs.counterapp.ui.models.CategoryUiModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCategoryViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {


    fun saveCategory(
        category: CategoryUiModel,
        onSuccess: (() -> Unit)? = null,
    ) {

        viewModelScope.launch {
            categoryUseCases.createCategory(CreateCategoryRequest.of(category.toDomain()))
        }

        onSuccess?.invoke()
    }
}