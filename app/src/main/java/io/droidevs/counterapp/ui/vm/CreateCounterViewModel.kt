package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.ui.models.CounterSnapshot
import io.droidevs.counterapp.ui.toDomain
import kotlinx.coroutines.launch

class CreateCounterViewModel(
    val repository : CounterRepository
) : ViewModel() {



    fun saveCounter(
        counter: CounterSnapshot,
        onCounterSaved: () -> Unit
    ) {
        viewModelScope.launch {
            repository.createCounter(counter.toDomain())
        }
        onCounterSaved()
    }
}