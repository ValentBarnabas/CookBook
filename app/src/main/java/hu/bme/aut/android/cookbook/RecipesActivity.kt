package hu.bme.aut.android.cookbook

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.android.cookbook.databinding.ActivityRecipesBinding
import hu.bme.aut.android.cookbook.ui.login.LoginFragment
import hu.bme.aut.android.cookbook.ui.dialogpopups.LogoutDialogFragment
import hu.bme.aut.android.cookbook.ui.myrecipes.MyRecipesFragment
import hu.bme.aut.android.cookbook.ui.othersrecipes.OthersRecipesFragment

//TODO: rate recipe (with popup alert), share (with popup window)
//TODO: add offline and anonymous recipe adding, FEEDBACK on successful delete

//TODO: implement being able to upload recipes if they are not uploaded to firebase (uID == 0)
//TODO: edit, hogy lehessen ugy is hozzaadni, hogy kivalasztjuk, csak magunknak akarjuk, csak masoknak, vagy mindketto

//TODO: szepitesek: bejelentkezesnel jelszo lathatosaga toggleelheto, egyseges kinezet, szebb szinek es elrendezesek. Uj recept kepe valaszthato galeriabol is, kep kitolti a helyet

class RecipesActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, LogoutDialogFragment.ResultDialogListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRecipesBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipesBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navView: NavigationView = findViewById(R.id.nav_view)
        binding.navView.setNavigationItemSelectedListener(this)

        var toggle : ActionBarDrawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if(savedInstanceState == null){     //Prevents changing fragment on device rotating
            supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, MyRecipesFragment()).commit()
            binding.navView.setCheckedItem(R.id.nav_myRecipes)
        }

        updateDrawerInformation()

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
                openLogoutDialog()
            }
            R.id.nav_login -> {
                supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, LoginFragment()).commit()
            }
            R.id.nav_myRecipes -> {
                supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, MyRecipesFragment()).commit()
            }
            R.id.nav_othersRecipes -> {
                supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, OthersRecipesFragment()).commit()
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun openLogoutDialog() {
        LogoutDialogFragment().show(supportFragmentManager, "logout dialog")
    }

    fun addOnFragment(fragment : Fragment) {        //Adds fragment to backstack, so we can leave it with back button
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.addToBackStack(null)
        binding.navView.setCheckedItem(fragment.id)
        transaction.commit()
    }

    fun addOnFragmentWithExtra(fragment: Fragment, extraParcelable: Parcelable){
        val bundle = Bundle()
        bundle.putParcelable("extra", extraParcelable)
        fragment.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.addToBackStack(null)
        binding.navView.setCheckedItem(fragment.id)
        transaction.commit()
    }

    fun swapToFragment(fragment: Fragment) {        //Swap current fragment to given one
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        binding.navView.setCheckedItem(fragment.id)
        transaction.commit()
    }

    fun updateDrawerInformation() {                 //Updates textView1 and textView2 in nav_header_main. Call in LoginFragment success and LogoutFragment success
        val navView: NavigationView = findViewById(R.id.nav_view)
        val headerView = navView.getHeaderView(0)
        val tvFirst: TextView = headerView.findViewById(R.id.navHeaderMain_tvFirstLine)
        val tvSecond: TextView = headerView.findViewById(R.id.navHeaderMain_tvSecondLine)
        val navMenu = navView.menu
        val navLogin = navMenu.findItem(R.id.nav_login)
        val navLogout = navMenu.findItem(R.id.nav_logout)
        if (FirebaseAuth.getInstance().currentUser == null) {
            tvFirst.text = getString(R.string.nav_header_title_no_auth)
            tvSecond.text = getString(R.string.nav_header_subtitle_no_auth)
            navLogin.isVisible = true
            navLogout.isVisible = false

        } else {
            tvFirst.text = FirebaseAuth.getInstance().currentUser.displayName.toString()
            tvSecond.text = FirebaseAuth.getInstance().currentUser.email.toString()
            navLogin.isVisible = false
            navLogout.isVisible = true
        }
    }

    override fun returnValue(bool: Boolean) {
        if(bool) {
            FirebaseAuth.getInstance().signOut()
            updateDrawerInformation()
            Toast.makeText(this, R.string.authentication_logout_successfull, Toast.LENGTH_LONG).show()
            startActivity(Intent(this, RecipesActivity::class.java))
        }
    }
}