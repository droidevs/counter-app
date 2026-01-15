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
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.window.layout.WindowMetricsCalculator
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigationrail.NavigationRailView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.databinding.ActivityMainBinding
import io.droidevs.counterapp.ui.fragments.CategoryListFragment
import io.droidevs.counterapp.ui.listeners.VolumeKeyHandler
import io.droidevs.counterapp.ui.message.UiMessage
import io.droidevs.counterapp.ui.message.actions.UiActionHandler
import io.droidevs.counterapp.ui.message.dispatcher.UiMessageDispatcher
import io.droidevs.counterapp.ui.message.mappers.toSnackbarLength
import io.droidevs.counterapp.ui.message.mappers.toToastLength
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    var binding : ActivityMainBinding? = null

    @Inject lateinit var messageDispatcher: UiMessageDispatcher

    @Inject lateinit var actionHandler: UiActionHandler

    private var toolbar : MaterialToolbar? = null
    private lateinit var navController : NavController;
    private lateinit var appBarConfiguration : AppBarConfiguration;

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var navRail: NavigationRailView
    private lateinit var drawer : DrawerLayout

    private var currentWidthClass = WindowWidthSizeClass.Compact


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Enable edge-to-edge drawing (draw behind system bars)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        
        setContentView(binding!!.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        toolbar = binding!!.mainToolbar
        //toolbar?.setOnMenuItemClickListener(this)
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

        var navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        // Restore state if activity was recreated
        if (savedInstanceState != null) {
            navController.restoreState(savedInstanceState.getBundle("nav_state"))
        }

        val topLevelDestinations = setOf(
            R.id.homeFragment,
            R.id.counterListFragment,
            R.id.categoryListFragment,
            R.id.historyFragment,
            R.id.settingsFragment
        )
        appBarConfiguration = AppBarConfiguration(
            topLevelDestinations,
            drawer
        )

//        NavigationUI.setupActionBarWithNavController(
//            this,
//            navController = navController,
//            configuration = appBarConfiguration
//        )
        bottomNav = binding!!.bottomNavigation

        //NavigationUI.setupWithNavController(bottomNav, navController)

        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNav.setupWithNavController(navController)
        //setupBottomNav()

        navController.addOnDestinationChangedListener { _, dest, _ ->
            if (dest.id in topLevelDestinations) {
                toolbar?.setNavigationOnClickListener { drawer.openDrawer(GravityCompat.START) }
            } else {
                toolbar?.setNavigationOnClickListener { navController.navigateUp() }
            }
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                messageDispatcher.flow().collect { message ->
                    handleMessage(message)
                }
            }
        }
        return super.onCreateView(name, context, attrs)
    }

    private fun handleMessage(message: UiMessage) {
        when (message) {

            is UiMessage.Toast -> {
                Toast.makeText(
                    this,
                    message.text,
                    message.duration.toToastLength()
                ).show()
            }

            is UiMessage.Snackbar -> {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    message.text,
                    message.duration.toSnackbarLength()
                ).apply {
                    message.action?.let { action ->
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

//    private fun setupBottomNav() {
//        bottomNav.setOnItemSelectedListener { item ->
//            when(item.itemId) {
//                R.id.home_graph -> {
//                    navController.navigate(R.id.action_to_home_graph)
//                    true
//                }
//                R.id.counters_graph -> {
//                    navController.navigate(R.id.action_to_home_graph)
//                    true
//                }
//                R.id.categories_graph -> {
//                    navController.navigate(R.id.categories_graph)
//                    true
//                }
//                R.id.settings_graph -> {
//                    navController.navigate(R.id.settingsFragment)
//                    true
//                }
//                else -> false
//            }
//        }
//    }

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


    // Navigate from drawer with arguments
    fun NavController.navigateFromDrawer(destinationId: Int, args: Bundle? = null) {
        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true) // Avoid multiple instances
            .setPopUpTo(
                destinationId = navController.graph.startDestinationId,
                inclusive = false
            ) // Keep bottom nav root intact
            .build()

        navController.navigate(destinationId, args, navOptions)
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
        when (item.itemId) {
            R.id.menuSettings -> {
                navController.navigate(R.id.action_to_settings_graph)
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(
            appBarConfiguration = appBarConfiguration
        ) || super.onSupportNavigateUp()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val bundle = findNavController(R.id.nav_host_fragment).saveState()
        outState.putBundle("nav_state", bundle)
    }


    private fun openHistory() {
        navController.navigate(R.id.action_to_history_graph)
    }

    private fun openSystemCategories() {
        navController.navigate(
            R.id.categories_graph,
            bundleOf("isSystem" to true)
        )

    }

    private fun rateApp() {
        // Open Play Store
    }

    private fun openAbout() {
        navController.navigate(R.id.action_to_about_graph)
    }

    private fun openSettings() {
        navController.navigate(R.id.action_to_settings_graph)
    }
}
