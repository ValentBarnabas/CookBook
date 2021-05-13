package hu.bme.aut.android.cookbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.android.cookbook.databinding.ActivityRecipesBinding
import hu.bme.aut.android.cookbook.ui.createrecipe.CreateRecipeFragment

//TODO: check if uploading and downloading recipes works, fix being able to open new recipe multiple times

class RecipesActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRecipesBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipesBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

//        binding.navView.setNavigationItemSelectedListener(this)   //TODO: ez ha be van rakva, akkor nem cserelodnek az oldalak, do something, or leave out

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(      //TODO: itt ha be van loginolva, akkor logout legyen, ha nincs akkor login
            setOf(
                R.id.nav_myRecipes, R.id.nav_othersRecipes, R.id.nav_login, R.id.nav_logout, R.id.nav_createRecipe
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        binding.fab.setOnClickListener {
//            val manager: FragmentManager = supportFragmentManager
//            val transaction = manager.beginTransaction()
//            transaction.replace(R.id.drawer_layout, CreateRecipeFragment())
//            transaction.addToBackStack(null)
//            transaction.commit()
//        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
//                Auth.GoogleSignInApi.signOut(apiClient);
            }
            R.id.nav_login -> {
//                startActivity(Intent(this@RecipesActivity, LoginActivity::class.java))  //Nem kell, nekem nem ebbol kezdodik, hanem megnyitodik csak kivanatra
            }
            R.id.nav_myRecipes -> {
                //get my recipes from persistent storage
            }
            R.id.nav_othersRecipes -> {
                //get others recipes from firebase
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

//    fun loadFragment(fragment: Fragment?) {
//        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.frame, fragment)
//        transaction.commit()
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {     //TODO: delete if i have no use for it
        menuInflater.inflate(R.menu.recipess, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}