package io.droidevs.counterapp.ui.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.ui.vm.CountersListViewModel

class CountersListViewModelFactory(
    var repository: CounterRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountersListViewModel::class.java))
            return CountersListViewModel(repository) as T
        else
            throw IllegalArgumentException("Unknown ViewModel class")
    }
}