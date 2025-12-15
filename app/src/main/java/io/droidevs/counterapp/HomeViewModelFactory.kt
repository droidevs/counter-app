package io.droidevs.counterapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.droidevs.counterapp.data.CounterRepository

class HomeViewModelFactory(
    private val repository: CounterRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
         throw IllegalArgumentException("Unknown ViewModel class")
    }
}