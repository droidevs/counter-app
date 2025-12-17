package io.droidevs.counterapp.data.fake

import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.domain.model.Counter
import java.time.Instant

object DummyData {

    fun getCounters(): List<Counter> = listOf(
        Counter(
            name = "Morning Routine",
            currentCount = 3,
            canIncrease = true,
            canDecrease = true,
            createdAt = Instant.now().minusSeconds(3600),
            lastUpdatedAt = Instant.now().minusSeconds(1800)
        ),
        Counter(
            name = "Water Intake",
            currentCount = 5,
            canIncrease = true,
            canDecrease = true,
            createdAt = Instant.now().minusSeconds(7200),
            lastUpdatedAt = Instant.now().minusSeconds(600)
        ),
        Counter(
            name = "Exercise Reps",
            currentCount = 12,
            canIncrease = true,
            canDecrease = true,
            createdAt = Instant.now().minusSeconds(10800),
            lastUpdatedAt = Instant.now().minusSeconds(3600)
        ),
        Counter(
            name = "Reading Pages",
            currentCount = 20,
            canIncrease = true,
            canDecrease = true,
            createdAt = Instant.now().minusSeconds(14400),
            lastUpdatedAt = Instant.now().minusSeconds(7200)
        ),
        Counter(
            name = "Meditation Minutes",
            currentCount = 15,
            canIncrease = true,
            canDecrease = true,
            createdAt = Instant.now().minusSeconds(18000),
            lastUpdatedAt = Instant.now().minusSeconds(9000)
        ),
        Counter(
            name = "Steps Walked",
            currentCount = 8000,
            canIncrease = true,
            canDecrease = true,
            createdAt = Instant.now().minusSeconds(21600),
            lastUpdatedAt = Instant.now().minusSeconds(1800)
        ),
        Counter(
            name = "Sleep Hours",
            currentCount = 6,
            canIncrease = true,
            canDecrease = true,
            createdAt = Instant.now().minusSeconds(25000),
            lastUpdatedAt = Instant.now().minusSeconds(12000)
        ),
        Counter(
            name = "Push-ups",
            currentCount = 30,
            canIncrease = true,
            canDecrease = true,
            createdAt = Instant.now().minusSeconds(27000),
            lastUpdatedAt = Instant.now().minusSeconds(15000)
        ),
        Counter(
            name = "Books Completed",
            currentCount = 2,
            canIncrease = true,
            canDecrease = true,
            createdAt = Instant.now().minusSeconds(30000),
            lastUpdatedAt = Instant.now().minusSeconds(5000)
        ),
        Counter(
            name = "Meals Tracked",
            currentCount = 7,
            canIncrease = true,
            canDecrease = true,
            createdAt = Instant.now().minusSeconds(33000),
            lastUpdatedAt = Instant.now().minusSeconds(1000)
        )
    )

    fun getCategories(): List<Category> = listOf(
        Category(id = "1", name = "Fitness", countersCount = 3),
        Category(id = "2", name = "Work", countersCount = 5),
        Category(id = "3", name = "Hobbies", countersCount = 2)
    )
}
