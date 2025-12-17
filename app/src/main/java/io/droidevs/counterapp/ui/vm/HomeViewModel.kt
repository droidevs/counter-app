package io.droidevs.counterapp.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.droidevs.counterapp.data.CounterRepository
import io.droidevs.counterapp.domain.toSnapshot
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(val counterRepository: CounterRepository) : ViewModel() {


    val countersSnapshots = counterRepository.getAllCounters()
        .onStart { emit(emptyList()) }
        .map { counters ->
            counters.map {
                it.toSnapshot()
            }
        }

    val countersNumber = counterRepository.getTotalCounters()
        .onStart { emit(0) }



}