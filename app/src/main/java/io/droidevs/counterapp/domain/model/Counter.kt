package io.droidevs.counterapp.domain.model
import java.time.Instant
import java.util.UUID

/**
 * Domain model representing a Counter.
 * Contains business rules and state.
 */
class Counter(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var currentCount: Int = 0,
    var canIncrease: Boolean = true,
    var canDecrease: Boolean = false,

    var categoryId: String? = null,
    var isSystem: Boolean = false,

    val createdAt: Instant = Instant.now(),
    var lastUpdatedAt: Instant? = null,
    var orderAnchorAt: Instant? = null
) {

    fun increment() {
        if (!canIncrease) return
        currentCount++
        touch()
    }

    fun decrement() {
        if (!canDecrease) return
        currentCount--
        touch()
    }

    fun reset() {
        currentCount = 0
        touch()
    }

    private fun touch() {
        lastUpdatedAt = Instant.now()
    }

    companion object {
        fun default() = Counter(
            name = "",
            currentCount = 0,
            canIncrease = true,
            canDecrease = false,
            categoryId = null
        )
    }

}

