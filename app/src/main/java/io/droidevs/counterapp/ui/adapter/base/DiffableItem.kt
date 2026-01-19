package io.droidevs.counterapp.ui.adapter.base

/**
 * Contract for list items shown by RecyclerView adapters.
 *
 * - [diffId] must be stable and unique per logical item.
 * - [diffContent] should contain the fields that affect UI equality.
 */
interface DiffableItem {
    val diffId: Any
    val diffContent: Any
}

