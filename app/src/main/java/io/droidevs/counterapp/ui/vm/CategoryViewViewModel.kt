package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import io.droidevs.counterapp.data.fake.DummyData
import io.droidevs.counterapp.data.toDomain
import io.droidevs.counterapp.ui.models.CategoryWithCountersUiModel
import io.droidevs.counterapp.ui.toSnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CategoryViewViewModel : ViewModel() {

    private val _category =
        MutableStateFlow<CategoryWithCountersUiModel?>(null)

    val category = _category.asStateFlow()

    init {
        loadCategory()
    }

    private fun loadCategory() {
        val model = CategoryWithCountersUiModel(
            categoryId = "1",
            categoryName = "Category 1",
            counters = DummyData.getCounters()
                .map { counter ->
                    counter.toDomain().toSnapshot()
                }
        )

        _category.value = model
    }
}

