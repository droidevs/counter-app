package io.droidevs.counterapp.data.fake

import io.droidevs.counterapp.data.CategoryEntity
import io.droidevs.counterapp.data.CounterEntity
import java.time.Instant
import java.util.UUID

// --- Dummy data provider ---
object DummyData {

    private val counterNames = listOf(
        "Morning Routine", "Water Intake", "Exercise Reps", "Reading Pages",
        "Meditation Minutes", "Steps Walked", "Sleep Hours", "Push-ups",
        "Books Completed", "Meals Tracked", "Daily Journaling", "Stretching",
        "Work Tasks", "Emails Sent", "Calls Made", "Project Updates",
        "Groceries Bought", "Cooking Meals", "Learning Hours", "Meditation Sessions",
        "Yoga Minutes", "Cycling Distance", "Running Distance", "Savings Added",
        "Pages Read", "Videos Watched", "Steps Run", "Push-ups Completed",
        "Sit-ups Completed", "Calories Burned", "Drinks Logged", "Tasks Done",
        "Notes Written", "Appointments Scheduled", "Meditation Goals"
    )

    private val categoryNames = listOf(
        "Fitness", "Work", "Hobbies", "Health", "Education", "Daily Routine",
        "Finance", "Leisure", "Social", "Spiritual"
    )

    fun getCounters(): List<CounterEntity> {
        val now = Instant.now()
        return List(35) { index ->
            CounterEntity(
                id = UUID.randomUUID().toString(),
                name = counterNames.getOrElse(index) { "Counter $index" },
                currentCount = (0..50).random(),
                canIncrement = true,
                canDecrement = (0..1).random() == 1,
                createdAt = now.minusSeconds((index * 3600).toLong()),
                lastUpdatedAt = now.minusSeconds((index * 1800).toLong())
            )
        }
    }

    fun getCategories(): List<CategoryEntity> {
        return List(8) { index ->
            CategoryEntity(
                id = UUID.randomUUID().toString(),
                name = categoryNames.getOrElse(index) { "Category $index" },
                countersCount = (1..10).random()
            )
        }
    }
}