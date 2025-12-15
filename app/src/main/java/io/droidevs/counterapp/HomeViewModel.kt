package io.droidevs.counterapp

import androidx.lifecycle.ViewModel
import io.droidevs.counterapp.model.CounterSnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import java.time.Instant

class HomeViewModel : ViewModel() {


    private val _countersSnapshots = MutableStateFlow<List<CounterSnapshot>>(emptyList())
    private val _countersNumber = MutableStateFlow(0)

    val countersSnapshots = _countersSnapshots
        .asStateFlow()
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
            CounterSnapshot(
                id = "1",
                name = "Water Intake",
                currentCount = 3,
                maxCount = 8,
                createdAt = Instant.now().minusSeconds(3600),
                lastUpdatedAt = Instant.now().minusSeconds(1800)
            ),
            CounterSnapshot(
                id = "2",
                name = "Push-ups",
                currentCount = 15,
                maxCount = 30,
                createdAt = Instant.now().minusSeconds(7200),
                lastUpdatedAt = Instant.now().minusSeconds(600)
            ),
            CounterSnapshot(
                id = "3",
                name = "Meditation",
                currentCount = 1,
                maxCount = 1,
                createdAt = Instant.now().minusSeconds(10800),
                lastUpdatedAt = Instant.now().minusSeconds(100)
            ),
            CounterSnapshot(
                id = "4",
                name = "Steps",
                currentCount = 5000,
                maxCount = 10000,
                createdAt = Instant.now().minusSeconds(14400),
                lastUpdatedAt = Instant.now().minusSeconds(300)
            ),
            CounterSnapshot(
                id = "5",
                name = "Reading",
                currentCount = 20,
                maxCount = null, // unlimited
                createdAt = Instant.now().minusSeconds(20000),
                lastUpdatedAt = Instant.now().minusSeconds(200)
            )
//            CounterSnapshot(
//                id = "6",
//                name = "Coffee Cups",
//                currentCount = 2,
//                maxCount = 5,
//                createdAt = Instant.now().minusSeconds(25000),
//                lastUpdatedAt = Instant.now().minusSeconds(150)
//            )
        )
        _countersSnapshots.value = dummyCounters
    }
}