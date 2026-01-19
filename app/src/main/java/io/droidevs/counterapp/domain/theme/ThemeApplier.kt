package io.droidevs.counterapp.domain.theme

import io.droidevs.counterapp.data.Theme

/**
 * Applies an app [Theme] to the UI framework (AppCompat).
 *
 * This is intentionally abstracted so MainActivity depends on an interface,
 * and the actual application mechanism can be swapped/tested.
 */
interface ThemeApplier {
    fun apply(theme: Theme)
}
