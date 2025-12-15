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

class HomeViewModel() : ViewModel() {

    val counterRepository: CounterRepository = CounterRepository()

    private val _countersSnapshots = MutableStateFlow<List<Counter>>(emptyList())
    private val _countersNumber = MutableStateFlow(0)

    val countersSnapshots = _countersSnapshots
        .asStateFlow()
        .map { counters ->
            counters.map {
                it.toSnapshot()
            }
        }

    val countersNumber = _countersNumber
        .asStateFlow()

    init {
        loadCounters()
        loadCountersNumber()
    }

    private fun loadCountersNumber() {
        // temporary fix todo: read real number from database
        _countersNumber.value = _countersSnapshots.value.size
    }

    private fun loadCounters() {
        viewModelScope.launch {
            var counters = counterRepository.getAllCounters().take(5)
            // for now it wont execute todo : populate db with dummy data
            if (counters.isNotEmpty())
                _countersSnapshots.value = counters
        }
    }
}