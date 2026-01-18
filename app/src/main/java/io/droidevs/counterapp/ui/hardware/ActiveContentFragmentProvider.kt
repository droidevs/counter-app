package io.droidevs.counterapp.ui.hardware

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Extracts the "currently visible content fragment" inside the active NavHostFragment.
 * MainActivity owns navigation, but this keeps the traversal logic testable and isolated.
 */
class ActiveContentFragmentProvider(
    private val activity: FragmentActivity,
) {
    fun current(): Fragment? {
        return activity.supportFragmentManager
            .primaryNavigationFragment
            ?.childFragmentManager
            ?.fragments
            ?.firstOrNull()
    }
}

