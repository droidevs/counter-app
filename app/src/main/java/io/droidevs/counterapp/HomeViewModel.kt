package io.droidevs.counterapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.counterapp.data.CounterRepository
import io.droidevs.counterapp.domain.toSnapshot
import io.droidevs.counterapp.model.Counter
import io.droidevs.counterapp.ui.CounterSnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.Instant

class HomeViewModel(val counterRepository: CounterRepository) : ViewModel() {


    private val _countersNumber = MutableStateFlow(0)

    val countersSnapshots = counterRepository.getAllCounters()
        .onStart { emit(emptyList()) }
        .map { counters ->
            counters.map {
                it.toSnapshot()
            }
        }

    val countersNumber = _countersNumber
        .asStateFlow()

    init {
        loadCountersNumber()
    }

    private fun loadCountersNumber() {
        // temporary fix todo: read real number from database
        _countersNumber.value = 0
    }

}