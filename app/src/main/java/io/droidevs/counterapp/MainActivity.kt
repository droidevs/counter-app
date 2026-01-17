package io.droidevs.counterapp

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import io.droidevs.counterapp.ui.navigation.tabs.MultiNavHostController
import io.droidevs.counterapp.ui.navigation.tabs.Tab
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigationrail.NavigationRailView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.databinding.ActivityMainBinding
import io.droidevs.counterapp.ui.listeners.VolumeKeyHandler
import io.droidevs.counterapp.ui.message.Message
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.actions.UiActionHandler
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.message.mappers.toSnackbarLength
import io.droidevs.counterapp.ui.message.mappers.toToastLength
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.window.layout.WindowMetricsCalculator
import io.droidevs.counterapp.ui.navigation.tabs.TabHost


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), TabHost {
    var binding : ActivityMainBinding? = null

    @Inject lateinit var messageDispatcher: UiMessageDispatcher

    @Inject lateinit var actionHandler: UiActionHandler

    private var toolbar : MaterialToolbar? = null
    private lateinit var navController : NavController
    private lateinit var appBarConfiguration : AppBarConfiguration

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var navRail: NavigationRailView
    private lateinit var drawer : DrawerLayout

    private var currentWidthClass = WindowWidthSizeClass.Compact

    private lateinit var tabsController: MultiNavHostController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        // Start collecting global UI messages once per Activity.
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                messageDispatcher.flow().collect { message ->
                    handleMessage(message)
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        toolbar = binding!!.mainToolbar
        setSupportActionBar(toolbar)

        drawer = binding!!.drawerLayout

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        setupDrawer()

        // Setup bottom nav / rail before NavController hookup.
        bottomNav = binding!!.bottomNavigation
        navRail = binding!!.navRail

        tabsController = MultiNavHostController(
            fragmentManager = supportFragmentManager,
            containerId = R.id.tab_nav_host_container,
            lifecycle = lifecycle
        )

        // Restore selected tab (default HOME).
        val initialTab = savedInstanceState
            ?.getInt(KEY_SELECTED_TAB_ID)
            ?.let { Tab.fromMenuId(it) }
            ?: Tab.HOME

        // Create + show the initial tab host.
        navController = tabsController.setup(initialTab)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.counterListFragment,
                R.id.categoryListFragment,
                R.id.settingsFragment
            ),
            drawer
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        // Hook bottom nav + rail to tab switching (NOT to a single NavController).
        fun selectTab(menuId: Int): Boolean {
            val tab = Tab.fromMenuId(menuId) ?: return false
            navController = tabsController.switchTo(tab)

            if (bottomNav.selectedItemId != tab.menuId) bottomNav.selectedItemId = tab.menuId
            if (navRail.selectedItemId != tab.menuId) navRail.selectedItemId = tab.menuId

            setupActionBarWithNavController(navController, appBarConfiguration)

            // Update toolbar immediately after switching tabs.
            updateToolbarNavIcon()

            // Ensure destination listener is attached to the active controller only.
            navController.addOnDestinationChangedListener { _, _, _ ->
                updateToolbarNavIcon()
            }

            return true
        }

        bottomNav.setOnItemSelectedListener { item ->
            selectTab(item.itemId)
        }
        navRail.setOnItemSelectedListener { item ->
            selectTab(item.itemId)
        }

        // Proper back behavior: close drawer first if open, else pop within current tab.
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                    return
                }

                val consumed = tabsController.popBackStack()
                if (!consumed) finish()
            }
        })

        // Navigation icon behavior depends on whether we can navigate up in the current tab.
        navController.addOnDestinationChangedListener { _, _, _ ->
            updateToolbarNavIcon()
        }

        updateToolbarNavIcon()
        updateNavigationForSize()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_TAB_ID, bottomNav.selectedItemId)
    }

    private fun isAtCurrentTabRoot(): Boolean {
        val currentDestId = navController.currentDestination?.id ?: return true
        val startDestId = navController.graph.startDestinationId
        return currentDestId == startDestId
    }

    private fun updateToolbarNavIcon() {
        if (isAtCurrentTabRoot()) {
            toolbar?.setNavigationOnClickListener { drawer.openDrawer(GravityCompat.START) }
        } else {
            toolbar?.setNavigationOnClickListener {
                navController.popBackStack()
            }
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        // Keep onCreateView lightweight: do not restart message collectors here.
        return super.onCreateView(name, context, attrs)
    }

    private fun handleMessage(uiMessage: UiMessage) {
        when (uiMessage) {

            is UiMessage.Toast -> {
                if (uiMessage.message is Message.Text)
                    Toast.makeText(
                        this,
                        uiMessage.message.value,
                        uiMessage.duration.toToastLength()
                    ).show()
                else if (uiMessage.message is Message.Resource) {
                    Toast.makeText(
                        this,
                        getString(uiMessage.message.resId, *uiMessage.message.args),
                        uiMessage.duration.toToastLength()
                    ).show()
                }
            }

            is UiMessage.Snackbar -> {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    uiMessage.text,
                    uiMessage.duration.toSnackbarLength()
                ).apply {
                    uiMessage.action?.let { action ->
                        actionHandler.handle(action = action.uiAction)
                    }
                }.show()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateNavigationForSize()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {

        if (event.action == KeyEvent.ACTION_DOWN) {
            val fragment = supportFragmentManager
                .primaryNavigationFragment
                ?.childFragmentManager
                ?.fragments
                ?.firstOrNull()


            if (fragment is VolumeKeyHandler) {
                when(event.keyCode) {
                    KeyEvent.KEYCODE_VOLUME_UP -> {
                        if (fragment.onVolumeUp()) return true
                    }
                    KeyEvent.KEYCODE_VOLUME_DOWN -> {
                        if (fragment.onVolumeDown()) return true
                    }
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }

    private fun setupDrawer() {
        binding!!.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.menu_history -> {
                    openHistory()
                }

                R.id.menu_system_category -> {
                    openSystemCategories()
                }

                R.id.menu_rate -> {
                    rateApp()
                }

                R.id.menu_about -> {
                    openAbout()
                }

                R.id.menu_settings -> {
                    openSettings()
                }
            }

            drawer.closeDrawer(GravityCompat.START)
            true
        }
    }


    private fun updateNavigationForSize() {
        val metrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
        val widthDp = metrics.bounds.width() / resources.displayMetrics.density

        val newClass = when {
            widthDp < 600 -> WindowWidthSizeClass.Compact
            widthDp < 840 -> WindowWidthSizeClass.Medium
            else -> WindowWidthSizeClass.Expanded
        }

        if (newClass != currentWidthClass) {
            currentWidthClass = newClass
            when (newClass) {
                WindowWidthSizeClass.Compact -> {
                    bottomNav.visibility = View.VISIBLE
                    navRail.visibility = View.GONE
                }

                else -> {  // Medium + Expanded: Use Rail
                    bottomNav.visibility = View.GONE
                    navRail.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuSettings -> {
                switchToTab(Tab.SETTINGS)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        // For tab navigation, treat navigate-up as back within the current tab.
        return navController.popBackStack() || super.onSupportNavigateUp()
    }

    // Drawer navigation should switch tabs or perform global navigation against the active tab.
    private fun openHistory() {
        // History is not a tab. Route it through a chosen tab; Settings is a good place.
        switchToTabAndNavigate(
            tab = Tab.SETTINGS,
            destinationId = R.id.historyFragment,
            args = null
        )
    }

    private fun openSystemCategories() {
        // Switch to Categories tab then open system mode.
        navController = tabsController.switchTo(Tab.CATEGORIES)
        if (bottomNav.selectedItemId != Tab.CATEGORIES.menuId) bottomNav.selectedItemId = Tab.CATEGORIES.menuId
        if (navRail.selectedItemId != Tab.CATEGORIES.menuId) navRail.selectedItemId = Tab.CATEGORIES.menuId

        navController.navigate(
            R.id.categoryListFragment,
            bundleOf("isSystem" to true)
        )
    }

    private fun rateApp() {
        // Open Play Store
    }

    private fun openAbout() {
        switchToTabAndNavigate(
            tab = Tab.SETTINGS,
            destinationId = R.id.aboutFragment,
            args = null
        )
    }

    private fun openSettings() {
        switchToTab(Tab.SETTINGS)
    }

    override fun switchToTab(tab: Tab) {
        navController = tabsController.switchTo(tab)
        if (bottomNav.selectedItemId != tab.menuId) bottomNav.selectedItemId = tab.menuId
        if (navRail.selectedItemId != tab.menuId) navRail.selectedItemId = tab.menuId
        setupActionBarWithNavController(navController, appBarConfiguration)
        updateToolbarNavIcon()
    }

    override fun switchToTabAndNavigate(tab: Tab, destinationId: Int, args: Bundle?) {
        switchToTab(tab)
        navController.navigate(destinationId, args)
    }

    private companion object {
        private const val KEY_SELECTED_TAB_ID = "selected_tab_id"
    }
}
