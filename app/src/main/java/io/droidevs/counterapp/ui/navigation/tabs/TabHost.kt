package io.droidevs.counterapp.ui.navigation.tabs

import android.os.Bundle

/**
 * Contract exposed by MainActivity to allow fragments to request
 * tab switching + navigation without directly coupling to MainActivity.
 */
interface TabHost {

    /**
     * Switches to the given tab while preserving back stacks.
     */
    fun switchToTab(tab: Tab)

    /**
     * Switches to the given tab and navigates to the tab's start destination.
     * Use this when you want a predictable landing screen (e.g. Home -> "All Categories").
     */
    fun switchToTabRoot(tab: Tab, args: Bundle? = null)

    /**
     * Switches to the given tab (if needed) then performs a navigation action
     * inside that tab's NavController.
     */
    fun switchToTabAndNavigate(
        tab: Tab,
        destinationId: Int,
        args: Bundle? = null
    )
}
