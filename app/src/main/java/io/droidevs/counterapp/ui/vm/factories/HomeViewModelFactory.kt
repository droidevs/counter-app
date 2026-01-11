package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.domain.usecases.counters.CounterUseCases
import io.droidevs.counterapp.domain.usecases.category.CategoryUseCases
import io.droidevs.counterapp.ui.vm.HomeViewModel

@Deprecated("Use Hilt injection with @HiltViewModel instead")
class HomeViewModelFactory(
    private val counterUseCases: CounterUseCases,
    private val categoryUseCases: CategoryUseCases
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                counterUseCases = counterUseCases,
                categoryUseCases = categoryUseCases
            ) as T
        }
         throw IllegalArgumentException("Unknown ViewModel class")
    }
}