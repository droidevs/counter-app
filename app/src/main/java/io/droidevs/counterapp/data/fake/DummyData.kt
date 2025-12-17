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

    private val categoryIds = List(categoryNames.size) { UUID.randomUUID().toString() }

    val counters: List<CounterEntity> by lazy { generateCounters() }

    val categories: List<CategoryEntity> by lazy { generateCategories() }

    private fun generateCounters(): List<CounterEntity> {
        val now = Instant.now()
        return counterNames.mapIndexed { index, name ->
            val categoryId = categoryIds.random() // round-robin
            CounterEntity(
                id = UUID.randomUUID().toString(),
                name = name,
                currentCount = (0..50).random(),
                canIncrement = true,
                canDecrement = (0..1).random() == 1,
                categoryId = categoryId,
                createdAt = now.minusSeconds((index * 3600).toLong()),
                lastUpdatedAt = now.minusSeconds((index * 1800).toLong())
            )
        }
    }

    private fun generateCategories(): List<CategoryEntity> {
        val categoryCounterMap = counters.groupingBy { it.categoryId }.eachCount()
        return categoryIds.mapIndexed { index, id ->
            CategoryEntity(
                id = id,
                name = categoryNames.getOrElse(index) { "Category $index" },
                countersCount = categoryCounterMap[id] ?: 0
            )
        }
    }

}