package com.example.projet_integration

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
//import com.coding.meet.navigationdrawerbottomnavigationbar.R
//import com.coding.meet.navigationdrawerbottomnavigationbar.bottom_fragment.CartFragment
//import com.coding.meet.navigationdrawerbottomnavigationbar.bottom_fragment.CategoryFragment
//import com.coding.meet.navigationdrawerbottomnavigationbar.bottom_fragment.HistoryFragment
//import com.coding.meet.navigationdrawerbottomnavigationbar.bottom_fragment.NotificationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Handle bottom navigation
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_category -> {
                    replaceFragment(CategoryFragment())
                    activity?.title = "Category"
                }
                R.id.bottom_history -> {
                    replaceFragment(HistoryFragment())
                    activity?.title = "History"
                }
                R.id.bottom_notification -> {
                    replaceFragment(NotificationFragment())
                    activity?.title = "Notification"
                }
                R.id.bottom_cart -> {
                    replaceFragment(CartFragment())
                    activity?.title = "Cart"
                }
            }
            true
        }

        // Default fragment
        replaceFragment(CategoryFragment())
        activity?.title = "Category"
        bottomNavigationView.selectedItemId = R.id.bottom_category

        // Handle FloatingActionButton click
        val addFab = view.findViewById<FloatingActionButton>(R.id.addFabBtn)
        addFab.setOnClickListener {
            showServiceDescriptionDialog()
        }

        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment, fragment)
            .commit()
    }

    // Function to show the dialog
    private fun showServiceDescriptionDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.add_service_freelancer, null)
        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false) // Prevent the user from dismissing the dialog by tapping outside

        val alertDialog = builder.show()

        // Bind views inside the dialog
        val closeButton: View = dialogView.findViewById(R.id.btnCloseDialog)
        val goToChatButton: View = dialogView.findViewById(R.id.btnGoToChat)

        // Handle dialog button clicks
        closeButton.setOnClickListener {
            alertDialog.dismiss()
        }

        goToChatButton.setOnClickListener {
            Toast.makeText(context, "Go to Chat Clicked", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss() // Close the dialog if necessary
        }
    }
}
