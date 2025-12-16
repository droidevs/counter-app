package io.droidevs.counterapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.MaterialToolbar
import io.droidevs.counterapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() , Toolbar.OnMenuItemClickListener{
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
        toolbar?.setOnMenuItemClickListener(this)
        setSupportActionBar(toolbar)

        var navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration.Builder(R.id.homeFragment).build()

        NavigationUI.setupActionBarWithNavController(
            this,
            navController = navController,
            configuration = appBarConfiguration
        )

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menuSettings -> {
                navController.navigate(R.id.action_home_to_settings)
            }
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            navController = navController,
            configuration = appBarConfiguration
        ) || super.onSupportNavigateUp()
    }
}