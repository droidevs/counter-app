package io.droidevs.counterapp

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowMetrics
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.enableSavedStateHandles
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.window.layout.WindowMetricsCalculator
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigationrail.NavigationRailView
import io.droidevs.counterapp.databinding.ActivityMainBinding
import io.droidevs.counterapp.ui.listeners.VolumeKeyHandler

class MainActivity : AppCompatActivity() {
    var binding : ActivityMainBinding? = null


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
        toolbar?.setNavigationOnClickListener { v: View? ->
            TODO("open a drawer")
        }
        //toolbar?.setOnMenuItemClickListener(this)
        setSupportActionBar(toolbar)

        drawer = binding!!.drawerLayout

        setupDrawer()

        var navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        // Restore state if activity was recreated
        if (savedInstanceState != null) {
            navController.restoreState(savedInstanceState.getBundle("nav_state"))
        }

        appBarConfiguration = AppBarConfiguration.Builder(R.id.homeFragment).build()

        NavigationUI.setupActionBarWithNavController(
            this,
            navController = navController,
            configuration = appBarConfiguration
        )
        var bottomNav = binding!!.bottomNavigation

        NavigationUI.setupWithNavController(bottomNav, navController)

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
        return NavigationUI.navigateUp(
            navController = navController,
            configuration = appBarConfiguration
        ) || super.onSupportNavigateUp()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val bundle = findNavController(R.id.nav_host_fragment).saveState()
        outState.putBundle("nav_state", bundle)
    }


    private fun openHistory() {
        // Navigate to history fragment
    }

    private fun openSystemCategories() {
        // Navigate to system categories fragment
    }

    private fun rateApp() {
        // Open Play Store
    }

    private fun openAbout() {
        // About screen
    }

    private fun openSettings() {
        // Settings screen
    }
}