package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.data.CategoryRepository
import io.droidevs.counterapp.data.CounterRepository

class HomeViewModelFactory(
    private val repository: CounterRepository,
    private val categoryRepository: CategoryRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                counterRepository = repository,
                categoryRepository = categoryRepository
                ) as T
        }
         throw IllegalArgumentException("Unknown ViewModel class")
    }
}