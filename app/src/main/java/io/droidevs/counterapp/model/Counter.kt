package io.droidevs.counterapp.model
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
    val createdAt: Instant = Instant.now(),
    var lastUpdatedAt: Instant = createdAt
) {

    /* -------------------- State Flags -------------------- */

    var canIncrease: Boolean = true
        private set

    var canDecrease: Boolean = false
        private set

    var isMaxReached: Boolean = false
        private set

    init {
        refreshFlags()
    }

    /* -------------------- Domain Logic -------------------- */

    fun increment() {
        if (!canIncrease) return
        currentCount++
        touch()
        refreshFlags()
    }

    fun decrement() {
        if (!canDecrease) return
        currentCount--
        touch()
        refreshFlags()
    }

    fun reset() {
        currentCount = 0
        touch()
        refreshFlags()
    }

    private fun touch() {
        lastUpdatedAt = Instant.now()
    }

    /* -------------------- Flag Calculation -------------------- */

    private fun refreshFlags() {
        canDecrease = currentCount > 0
        canIncrease = !isMaxReached
    }
}

