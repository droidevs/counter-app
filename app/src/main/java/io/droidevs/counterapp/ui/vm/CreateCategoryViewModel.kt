package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import io.droidevs.counterapp.ui.models.CategoryUiModel

class CreateCategoryViewModel : ViewModel() {


    fun saveCategory(
        category: CategoryUiModel,
        onSuccess: (() -> Unit)? = null,
    ) {

        // Dummy save (no repository yet)
        onSuccess?.invoke()
    }
}