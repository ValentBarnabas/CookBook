package hu.bme.aut.android.cookbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.android.cookbook.databinding.ActivityRecipesBinding


class RecipesActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRecipesBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipesBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setSupportActionBar(binding.appBarRecipes.toolbar)

        binding.appBarRecipes.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
//        binding.navView.setNavigationItemSelectedListener(this)   //TODO: ez ha be van rakva, akkor nem cserelodnek az oldalak, do something, or leave out

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(      //TODO: itt ha be van loginolva, akkor logout legyen, ha nincs akkor login
            setOf(
                R.id.nav_myRecipes, R.id.nav_othersRecipes, R.id.nav_login, R.id.nav_logout
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
//                startActivity(Intent(this, ))
//                finish()
            }
            R.id.nav_login -> {
//                startActivity(Intent(this@RecipesActivity, LoginActivity::class.java))  //Nem kell, nekem nem ebbol kezdodik, hanem megnyitodik csak kivanatra
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