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
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
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
import io.droidevs.counterapp.ui.navigation.AppNavigator


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    var binding : ActivityMainBinding? = null

    @Inject lateinit var messageDispatcher: UiMessageDispatcher

    @Inject lateinit var actionHandler: UiActionHandler

    @Inject lateinit var appNavigator: AppNavigator

    private var toolbar : MaterialToolbar? = null
    private lateinit var navController : NavController
    private lateinit var appBarConfiguration : AppBarConfiguration

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var navRail: NavigationRailView
    private lateinit var drawer : DrawerLayout

    private var currentWidthClass = WindowWidthSizeClass.Compact


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

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

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

        bottomNav = binding!!.bottomNavigation
        navRail = binding!!.navRail

        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNav.setupWithNavController(navController)
        navRail.setupWithNavController(navController)

        // Proper back behavior: close drawer first if open.
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = navController.popBackStack()
                    if (!isEnabled) {
                        finish()
                    }
                }
            }
        })

        navController.addOnDestinationChangedListener { _, dest, _ ->
            if (dest.id in topLevelDestinations) {
                toolbar?.setNavigationOnClickListener { drawer.openDrawer(GravityCompat.START) }
            } else {
                toolbar?.setNavigationOnClickListener {
                    NavigationUI.navigateUp(navController, appBarConfiguration)
                }
            }
        }

        updateNavigationForSize()
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
        when (item.itemId) {
            R.id.menuSettings -> {
                navController.navigate(R.id.action_to_settings_graph)
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun openHistory() {
        appNavigator.navigateRoot(R.id.action_to_history_graph)
    }

    private fun openSystemCategories() {
        appNavigator.navigateRoot(
            R.id.categories_graph,
            bundleOf("isSystem" to true)
        )
    }

    private fun rateApp() {
        // Open Play Store
    }

    private fun openAbout() {
        appNavigator.navigateRoot(R.id.action_to_about_graph)
    }

    private fun openSettings() {
        appNavigator.navigateRoot(R.id.action_to_settings_graph)
    }
}
