package io.droidevs.counterapp.repository

import io.droidevs.counterapp.data.entities.CategoryEntity
import io.droidevs.counterapp.data.entities.CounterEntity
import io.droidevs.counterapp.data.entities.HistoryEventEntity
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

// Change to class and add @Inject
@Singleton // Ensures the state inside is shared across the app
class DummyData @Inject constructor() {

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
    private val counterIds = List(counterNames.size) { UUID.randomUUID().toString() }

    val counters: MutableList<CounterEntity> by lazy { generateCounters() }
    val countersFlow = MutableStateFlow(emptyList<CounterEntity>())

    val categories: MutableList<CategoryEntity> by lazy { generateCategories() }
    val categoriesFlow = MutableStateFlow(emptyList<CategoryEntity>())

    val historyEvents: MutableList<HistoryEventEntity> by lazy { generateHistoryEvents() }
    val historyEventsFlow = MutableStateFlow(emptyList<HistoryEventEntity>())


    init {
        // Initialize flows after lazy initialization
        countersFlow.value = counters.toList()
        categoriesFlow.value = categories.toList()
        historyEventsFlow.value = historyEvents.toList()
    }

    private fun generateCounters(): MutableList<CounterEntity> {
        val now = Instant.now()
        return counterNames.mapIndexed { index, name ->
            val categoryId = categoryIds.random()
            CounterEntity(
                id = counterIds[index],
                name = name,
                currentCount = (0..50).random(),
                canIncrement = true,
                canDecrement = (0..1).random() == 1,
                categoryId = categoryId,
                createdAt = now.minusSeconds((index * 3600).toLong()),
                lastUpdatedAt = now.minusSeconds((index * 1800).toLong()),
                orderAnchorAt = now.minusSeconds((index * 1800).toLong())
            )
        }.toMutableList()
    }

    private fun generateCategories(): MutableList<CategoryEntity> {
        val categoryCounterMap = counters.groupingBy { it.categoryId }.eachCount()
        return categoryIds.mapIndexed { index, id ->
            CategoryEntity(
                id = id,
                name = categoryNames.getOrElse(index) { "Category $index" },
                countersCount = categoryCounterMap[id] ?: 0,
                color = 0
            )
        }.toMutableList()
    }

    private fun generateHistoryEvents(): MutableList<HistoryEventEntity> {
        val now = Instant.now()
        return counterIds.flatMap { counterId ->
            (1..Random.nextInt(1, 5)).map {
                val oldValue = Random.nextInt(0, 50)
                val change = Random.nextInt(1, 5)
                val newValue = oldValue + change
                HistoryEventEntity(
                    counterId = counterId,
                    oldValue = oldValue,
                    newValue = newValue,
                    change = change,
                    timestamp = now.minusSeconds(Random.nextLong(0, 3600 * 24 * 5))
                )
            }
        }.toMutableList()
    }


    fun emitCounterUpdate() {
        countersFlow.value = counters.toList()
    }

    fun emitCategoryUpdate() {
        categoriesFlow.value = categories.toList()
    }

    fun emitHistoryUpdate() {
        historyEventsFlow.value = historyEvents.toList()
    }
}
