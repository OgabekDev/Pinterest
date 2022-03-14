package dev.ogabek.pinterest.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.ogabek.pinterest.R
import dev.ogabek.pinterest.fragment.HomeFragment
import dev.ogabek.pinterest.fragment.MessageFragment
import dev.ogabek.pinterest.fragment.ProfileFragment
import dev.ogabek.pinterest.fragment.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var frameMain: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController!!.setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS)
        }

        setContentView(R.layout.activity_main)

        initViews()

    }

    private fun initViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        frameMain = findViewById(R.id.fl_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_main, HomeFragment())
            .commit()

        bottomNavigation.setOnNavigationItemSelectedListener(navListener)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.fl_main) is HomeFragment) {
            super.onBackPressed()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_main, HomeFragment())
                .commit()

        }
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_home -> {
                    selectedFragment = HomeFragment()
                }
                R.id.nav_search -> {
                    selectedFragment = SearchFragment()
                }
                R.id.nav_message -> {
                    selectedFragment = MessageFragment()
                }
                R.id.nav_profile -> {
                    selectedFragment = ProfileFragment()
                }
            }
            supportFragmentManager.beginTransaction().replace(
                R.id.fl_main,
                selectedFragment!!
            ).commit()
            true
        }

}