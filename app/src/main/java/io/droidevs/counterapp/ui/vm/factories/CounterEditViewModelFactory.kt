package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.vm.CounterEditViewModel

class CounterEditViewModelFactory(
    private val initialCounter : CounterUiModel,
    private val repository: CounterRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CounterEditViewModel::class.java)) {
            return CounterEditViewModel(initialCounter = initialCounter, repository = repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}