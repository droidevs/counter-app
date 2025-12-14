package io.droidevs.counterapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import io.droidevs.counterapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() , Toolbar.OnMenuItemClickListener{
    var binding : ActivityMainBinding? = null

    private var toolbar : MaterialToolbar? = null

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

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.counter_menu, menu)
        return true
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_edit -> {
                TODO("edit counter")
            }
            R.id.menu_delete -> {
                TODO("delete counter")
            }
            R.id.menu_reset -> {
                TODO("reset counter")
            }
        }
        return true
    }
}