package hu.bme.aut.android.cookbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.android.cookbook.databinding.ActivityRecipesBinding
import hu.bme.aut.android.cookbook.ui.login.LoginFragment
import hu.bme.aut.android.cookbook.ui.logout.LogoutFragment
import hu.bme.aut.android.cookbook.ui.myrecipes.MyRecipesFragment
import hu.bme.aut.android.cookbook.ui.othersrecipes.OthersRecipesFragment

//TODO: fix MyRecipesFragment.initRecipesListener, or whatever causes the crashes
//TODO: itt ha be van loginolva, akkor logout legyen, ha nincs akkor login. Belogolva a felhasznalo neve latsszon a nav draweren, kilogolva az Anonim
//TODO: implement opening recipes by clicking on them

class RecipesActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRecipesBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipesBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navView: NavigationView = findViewById(R.id.nav_view)   //TODO: remove these lines, check if works
        binding.navView.setNavigationItemSelectedListener(this)

        var toggle : ActionBarDrawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if(savedInstanceState == null){     //Prevents changing fragment on device rotating
            supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, MyRecipesFragment()).commit()
            binding.navView.setCheckedItem(R.id.nav_myRecipes)
        }

        if(FirebaseAuth.getInstance().currentUser == null) {
            val tvFirst : TextView? = findViewById(R.id.navHeaderMain_tvFirstLine)
            val tvSecond: TextView? = findViewById(R.id.navHeaderMain_tvSecondLine)
            tvFirst?.text = R.string.nav_header_title_no_auth.toString()
            tvSecond?.text = R.string.nav_header_subtitle_no_auth.toString()
        } else {
            val tvFirst : TextView? = findViewById(R.id.navHeaderMain_tvFirstLine)
            val tvSecond: TextView? = findViewById(R.id.navHeaderMain_tvSecondLine)
            tvFirst?.text = FirebaseAuth.getInstance().currentUser.uid.toString()
            tvSecond?.text = FirebaseAuth.getInstance().currentUser.email.toString()
        }

    }

    override fun onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_logout -> {
                supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, LogoutFragment()).commit()
//                FirebaseAuth.getInstance().signOut()
//                Auth.GoogleSignInApi.signOut(apiClient);
            }
            R.id.nav_login -> {
                supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, LoginFragment()).commit()

//                startActivity(Intent(this@RecipesActivity, LoginActivity::class.java))  //Nem kell, nekem nem ebbol kezdodik, hanem megnyitodik csak kivanatra
            }
            R.id.nav_myRecipes -> {
                supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, MyRecipesFragment()).commit()

                //get my recipes from persistent storage
            }
            R.id.nav_othersRecipes -> {
                supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, OthersRecipesFragment()).commit()

                //get others recipes from firebase
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {     //TODO: delete if i dont use it
        menuInflater.inflate(R.menu.recipess, menu)
        return true
    }

    fun addOnFragment(fragment : Fragment) {        //Adds fragment to backstack, so we can leave it with back button
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.addToBackStack(null)
        binding.navView.setCheckedItem(fragment.id)
        transaction.commit()
    }
    fun swapToFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        binding.navView.setCheckedItem(fragment.id)
        transaction.commit()
    }
}