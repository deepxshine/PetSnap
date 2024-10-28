package com.example.petsnap.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.petsnap.R
import com.example.petsnap.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navBottomView: BottomNavigationView = binding.navBottomView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf( //  top level destinations - back button is not needed for these destinations
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_create,
                R.id.navigation_friends,
                R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navBottomView.setupWithNavController(navController)

    }
}
/*    // add button on ActionBar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_right_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_right_button -> {

                when(val currentDestinationId = navController.currentDestination?.id) {

                    Log.d("MainActivity", "${currentDestinationId}"),
                    Log.d("MainActivity", "Enter into fragment determination"),

                    R.id.navigation_home -> {
                        handleHomeButtonClick()
                    }
                    R.id.navigation_search -> {
                        handleSearchButtonClick()
                    }
                    R.id.navigation_friends -> {
                        handleFriendsButtonClick()
                    }
                    R.id.navigation_profile -> {
                        handleProfileButtonClick()
                    }
                }

                true // означает, что данный клик уже обработан
            }
            else -> super.onOptionsItemSelected(item) // иначе вызываем onOptionsItemSelected
        }
    }

    private fun handleHomeButtonClick() {

        Toast.makeText(this, "Home Button Clicked", Toast.LENGTH_SHORT).show()
        val bottomSheetDialog = BottomSheetDialog(this)

        // способ 1: через view
        // val view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_create_post, bottomSheetDialog.findViewById(android.R.id.content), false)
            // bottomSheetDialog.findViewById(android.R.id.content) - Activity root
       // bottomSheetDialog.setContentView(view)

        // способ 2: через ViewBinding
        val binding: BottomSheetCreatePostBinding = BottomSheetCreatePostBinding.inflate(LayoutInflater.from(this), bottomSheetDialog.findViewById(android.R.id.content), false)

        bottomSheetDialog.setContentView(binding.root)

        //Button click listeners
//        binding.buttonTakePhotos.setOnClickListener {
//            handleButtonClick(it)
//        }
//
//        binding.buttonSelectFromAlbums.setOnClickListener {
//            handleButtonClick(it)
//        }
//
//        binding.buttonCancel.setOnClickListener {
//            handleButtonClick(it)
//        }

        bottomSheetDialog.show()
    }

    private fun handleSearchButtonClick() {
        // 处理 SearchFragment 中的按钮点击事件
        Toast.makeText(this, "Search Button Clicked", Toast.LENGTH_SHORT).show()
    }

    private fun handleFriendsButtonClick() {
        // 处理 FriendsFragment 中的按钮点击事件
        Toast.makeText(this, "Friends Button Clicked", Toast.LENGTH_SHORT).show()
    }

    private fun handleProfileButtonClick() {
        // 处理 ProfileFragment 中的按钮点击事件
        Toast.makeText(this, "Profile Button Clicked", Toast.LENGTH_SHORT).show()
    }

}

*/
