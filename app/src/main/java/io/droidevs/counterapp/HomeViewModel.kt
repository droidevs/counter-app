package io.droidevs.counterapp

import androidx.lifecycle.ViewModel
import io.droidevs.counterapp.domain.toSnapshot
import io.droidevs.counterapp.model.Counter
import io.droidevs.counterapp.ui.CounterSnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.time.Instant

class HomeViewModel : ViewModel() {


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
        val dummyCounters = listOf(
            Counter(
                id = "1",
                name = "Water Intake",
                currentCount = 3,
                createdAt = Instant.now().minusSeconds(3600),
                lastUpdatedAt = Instant.now().minusSeconds(1800)
            ),
            Counter(
                id = "2",
                name = "Push-ups",
                currentCount = 15,
                createdAt = Instant.now().minusSeconds(7200),
                lastUpdatedAt = Instant.now().minusSeconds(600)
            ),
            Counter(
                id = "3",
                name = "Meditation",
                currentCount = 1,
                createdAt = Instant.now().minusSeconds(10800),
                lastUpdatedAt = Instant.now().minusSeconds(100)
            ),
            Counter(
                id = "4",
                name = "Steps",
                currentCount = 5000,
                createdAt = Instant.now().minusSeconds(14400),
                lastUpdatedAt = Instant.now().minusSeconds(300)
            ),
            Counter(
                id = "5",
                name = "Reading",
                currentCount = 20,
                createdAt = Instant.now().minusSeconds(20000),
                lastUpdatedAt = Instant.now().minusSeconds(200)
            )
        )
        _countersSnapshots.value = dummyCounters
    }
}