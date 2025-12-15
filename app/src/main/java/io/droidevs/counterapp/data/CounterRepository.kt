package io.droidevs.counterapp.data

import io.droidevs.counterapp.model.Counter
import java.time.Instant

class CounterRepository() {

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
    /* -------------------- Read -------------------- */

    suspend fun getAllCounters(): List<Counter> {
        return dummyCounters
    }

}
