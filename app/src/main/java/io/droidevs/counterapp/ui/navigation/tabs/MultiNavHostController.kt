package io.droidevs.counterapp.ui.navigation.tabs

import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

/**
 * Manages multiple NavHostFragments (one per tab) to provide stateful back stacks.
 *
 * This is the classic "multiple back stacks" pattern:
 * - one NavHostFragment per tab
 * - show/hide + attach/detach on tab switch
 * - each host preserves its own back stack
 */
class MultiNavHostController(
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) {

    val currentTab: Tab?
        get() = currentTabInternal

    private var currentTabInternal: Tab? = null

    private val tabHosts = mutableMapOf<Tab, NavHostFragment>()

    val currentNavController: NavController?
        get() = currentTabInternal?.let { tabHosts[it]?.navController }

    fun setup(initialTab: Tab): NavController {
        // Restore existing hosts if they were recreated by FragmentManager (process death).
        Tab.entries.forEach { tab ->
            val tag = tagFor(tab)
            val existing = fragmentManager.findFragmentByTag(tag) as? NavHostFragment
            if (existing != null) {
                tabHosts[tab] = existing
            }
        }

        // Create missing hosts.
        Tab.entries.forEach { tab ->
            tabHosts.getOrPut(tab) {
                NavHostFragment.create(tab.graphResId)
            }
        }

        // Add any newly created hosts and show only the initial tab.
        val transaction = fragmentManager.beginTransaction().setReorderingAllowed(true)
        Tab.entries.forEach { tab ->
            val tag = tagFor(tab)
            val host = tabHosts.getValue(tab)

            if (!host.isAdded) {
                transaction.add(containerId, host, tag)
            }

            if (tab == initialTab) {
                transaction.setPrimaryNavigationFragment(host)
                transaction.show(host)
            } else {
                transaction.hide(host)
            }
        }

        transaction.commitNow()

        currentTabInternal = initialTab
        return tabHosts.getValue(initialTab).navController
    }

    fun switchTo(tab: Tab): NavController {
        if (currentTabInternal == tab) return tabHosts.getValue(tab).navController

        val newHost = tabHosts.getValue(tab)
        val oldHost = currentTabInternal?.let { tabHosts[it] }

        fragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            oldHost?.let { hide(it) }
            show(newHost)
            setPrimaryNavigationFragment(newHost)
        }.commitNow()

        currentTabInternal = tab
        return newHost.navController
    }

    /**
     * Handle back press.
     * @return true if consumed by popping within the current tab.
     */
    fun popBackStack(): Boolean {
        val nav = currentNavController ?: return false
        return nav.popBackStack()
    }

    private fun tagFor(tab: Tab) = "tab_host_${tab.name}"
}
