package io.droidevs.counterapp.domain.display

/**
 * Domain model for display-related preferences.
 *
 * - hideControls: hides +/- actions on counter rows.
 * - hideLastUpdate: hides "last updated" time UI.
 * - hideCounterCategoryLabel: hides the category label/chip on counter rows.
 */
data class DisplayPreferences(
    val hideControls: Boolean,
    val hideLastUpdate: Boolean,
    val hideCounterCategoryLabel: Boolean,
)

