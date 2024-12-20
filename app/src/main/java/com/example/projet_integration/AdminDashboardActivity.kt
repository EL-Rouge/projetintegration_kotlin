package com.example.projet_integration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.projet_integration.UserListFragment
import com.example.projet_integration.ServiceRequestFragment // Import your ServiceRequestFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_dashboard)

        // Initialize BottomNavigationView
        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Handle navigation item selection
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_user_manipulation -> {
                    loadFragment(UserListFragment())  // Load the User List fragment
                    true
                }

                R.id.bottom_service_requests -> {
                    loadFragment(ServiceRequestFragment()) // Load the Service Request fragment
                    true
                }

                else -> false
            }
        }

        // Load the default fragment when the activity starts
        if (savedInstanceState == null) {
            bottomNavigation.selectedItemId = R.id.bottom_user_manipulation
            loadFragment(UserListFragment()) // Default fragment
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.bottomFragmentAdmin, fragment)
            .commit()
    }
}
