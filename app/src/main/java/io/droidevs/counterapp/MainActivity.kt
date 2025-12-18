package io.droidevs.counterapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.MaterialToolbar
import io.droidevs.counterapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var binding : ActivityMainBinding? = null


    private var toolbar : MaterialToolbar? = null
    private lateinit var navController : NavController;
    private lateinit var appBarConfiguration : AppBarConfiguration;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        
        setContentView(binding!!.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toolbar = binding!!.mainToolbar
        toolbar?.setNavigationOnClickListener { v: View? ->
            TODO("open a drawer")
        }
        //toolbar?.setOnMenuItemClickListener(this)
        setSupportActionBar(toolbar)

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
}