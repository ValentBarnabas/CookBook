package hu.bme.aut.android.cookbook

import android.content.Context
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
import androidx.fragment.app.FragmentManager
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import hu.bme.aut.android.cookbook.databinding.ActivityRecipesBinding
import hu.bme.aut.android.cookbook.notification.FirebaseService
import hu.bme.aut.android.cookbook.notification.TOPIC
import hu.bme.aut.android.cookbook.ui.login.LoginFragment
import hu.bme.aut.android.cookbook.ui.dialogpopups.LogoutDialogFragment
import hu.bme.aut.android.cookbook.ui.myrecipes.MyRecipesFragment
import hu.bme.aut.android.cookbook.ui.othersrecipes.OthersRecipesFragment

//TODO: #1 rate recipe with POPUP ALERT
// vagy, ha egy adott etel eler mondjuk 100 likeot, akkor kuldunk mindenkinek egy ertesitest, hogy ez nagyon jora ertekelt, probaljak ki!!!

//TODO: #2 add offline and anonymous recipe adding :! Csere a MyRecipesFragment-ben a tarolast, nem URL-t tarol egyik sem, mindegyik deviceon levo kepnek az utvonalat. Kesziteskor elmeneti a kepet egy helyre, es az utvonalat eltarolja stringben.
// feltolteskor ezt adja meg, hogy ebbol csinaljanak glideolhato dolgot, letolteskor pedig a glideolhatot menti el eszkozre, es tarolja el az utvonalat. Szoval recept letrehozaskor devicera elmenti a kepet, es utvonalat tarol, torlesnel kitorli a kepet is, megjelenitesnel pedig a filet jeleniti meg oda
// offline es anonymous modon csak perzisztens tarolora lehet menteni

//TODO: szepitesek: bejelentkezesnel jelszo lathatosaga toggleelheto, elrendezesek. Uj recept kepe valaszthato galeriabol is, kep kitolti a helyet
//TODO: share/upload (with popup window, firebaseID == 0)
//TODO: edit, hogy lehessen ugy is hozzaadni, hogy kivalasztjuk, csak magunknak akarjuk, csak masoknak, vagy mindketto

class RecipesActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, LogoutDialogFragment.ResultDialogListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRecipesBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipesBinding.inflate(LayoutInflater.from(this))
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

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
                supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, LoginFragment()).commit()
            }
            R.id.nav_myRecipes -> {
                supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, MyRecipesFragment()).commit()
            }
            R.id.nav_othersRecipes -> {
                supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
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

    fun swapToFragmentWithExtra(fragment: Fragment, extraParcelable: Parcelable) {
        val bundle = Bundle()
        bundle.putParcelable("extra", extraParcelable)
        fragment.arguments = bundle

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
            supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            startActivity(Intent(this, RecipesActivity::class.java))
        }
    }
}