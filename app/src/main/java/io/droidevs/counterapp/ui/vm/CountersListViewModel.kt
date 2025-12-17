package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import io.droidevs.counterapp.domain.repository.CounterRepository
import io.droidevs.counterapp.domain.toSnapshot
import io.droidevs.counterapp.ui.CounterSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class CountersListViewModel(
    private val repository: CounterRepository
) : ViewModel() {

    val counters: Flow<List<CounterSnapshot>> = repository.getAllCounters()
        .onStart { emit(emptyList()) }
        .map { counters ->
            counters.map {
                it.toSnapshot()
            }
        }


}
