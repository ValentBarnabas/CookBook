package hu.bme.aut.android.cookbook.ui.viewrecipe

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.cookbook.Extensions.isOnline
import hu.bme.aut.android.cookbook.R
import hu.bme.aut.android.cookbook.RecipesActivity
import hu.bme.aut.android.cookbook.data.Recipe
import hu.bme.aut.android.cookbook.databinding.FragmentViewpersistentrecipeBinding
import hu.bme.aut.android.cookbook.ui.dialogpopups.DeleteRecipeDialogFragment
import hu.bme.aut.android.cookbook.ui.myrecipes.MyRecipesFragment
import hu.bme.aut.android.cookbook.viewmodel.RecipeViewModel

class ViewPersistentRecipeFragment : Fragment(), DeleteRecipeDialogFragment.ResultDialogListener {

    private var _binding: FragmentViewpersistentrecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var currRecipe: Recipe

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewpersistentrecipeBinding.inflate(inflater, container, false)
        val root = binding.root

        currRecipe = arguments?.getParcelable<Recipe>("extra")!!

        setUpView()

        setHasOptionsMenu(true)     //shows options menu

        return root
    }

    private fun setUpView() {
        var requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)
        Glide.with(requireContext()).load(currRecipe.imageUrl).apply(requestOptions)
            .into(binding.fragmentViewPersistentRecipeIvImage)

        binding.fragmentViewPersistentRecipeTvTitle.text = currRecipe.title
        binding.fragmentViewPersistentRecipeTvAuthor.text = getString(R.string.fragment_viewANYRecipe_RecipeBy) + " " + currRecipe.author
        binding.fragmentViewPersistentRecipeTvIngredients.text = currRecipe.ingredients
        binding.fragmentViewPersistentRecipeTvMethod.text = currRecipe.method

        binding.fragmentViewPersistentRecipeBtnUpload.isVisible = (currRecipe.firebaseID == "0")

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu_viewpersistentrecipefragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.options_menu_persistentrecipefragment_deleteForMyselfOption -> {
                var dialog = DeleteRecipeDialogFragment()
                var bundle = Bundle()
                bundle.putString("text", getString(R.string.popup_dialog_deleteForMyself))
                dialog.setTargetFragment(this, 0)
                dialog.arguments = bundle
                fragmentManager?.let { dialog.show(it, "delete for myself") }
            }
            R.id.options_menu_persistentrecipefragment_deleteForOthersOption -> {
                var dialog = DeleteRecipeDialogFragment()
                var bundle = Bundle()
                bundle.putString("text", getString(R.string.popup_dialog_deleteForOthers))
                dialog.setTargetFragment(this, 0)
                dialog.arguments = bundle
                fragmentManager?.let { dialog.show(it, "delete for others") }
            }
            R.id.options_menu_persistentrecipefragment_deleteForEveryoneOption -> {
                var dialog = DeleteRecipeDialogFragment()
                var bundle = Bundle()
                bundle.putString("text", getString(R.string.popup_dialog_deleteForEveryone))
                dialog.setTargetFragment(this, 0)
                dialog.arguments = bundle
                fragmentManager?.let { dialog.show(it, "delete for everyone") }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun returnValue(bool: Boolean, tag: String) {
        if (bool) {
            if (tag == "delete for myself") {
                deleteOffline()
            } else if (tag == "delete for others") {
                if(isOnline(requireContext())) {
                    if(FirebaseAuth.getInstance().currentUser != null && currRecipe.author == FirebaseAuth.getInstance().currentUser.displayName) {
                        deleteOnline()
                    } else {
                        Toast.makeText(requireContext(), R.string.fragment_deleteOnlineRecipeUnauthorizedUser, Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(requireContext(), R.string.fragment_deleteOnlineRecipeNoInternetAccess, Toast.LENGTH_LONG).show()
                }
            } else if (tag == "delete for everyone") {
                if(isOnline(requireContext())) {
                    if(FirebaseAuth.getInstance().currentUser != null && currRecipe.author == FirebaseAuth.getInstance().currentUser.displayName) {
                        deleteOnline()
                        deleteOffline()
                    } else {
                        Toast.makeText(requireContext(), R.string.fragment_deleteOnlineRecipeUnauthorizedUser, Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(requireContext(), R.string.fragment_deleteOnlineRecipeNoInternetAccess, Toast.LENGTH_LONG).show()
                }
            }
            (activity as RecipesActivity).swapToFragment(MyRecipesFragment())
        }
    }

    private fun deleteOffline() {
        RecipeViewModel().delete(currRecipe)
        Toast.makeText(requireContext(), R.string.toast_deleteOfflineRecipeSuccess, Toast.LENGTH_LONG).show()
    }
    private fun deleteOnline() {
        Firebase.firestore.collection("recipes").document(currRecipe.firebaseID!!).delete()
            .addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful) { Toast.makeText(requireContext(),R.string.toast_deleteOnlineRecipeSuccess, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(),R.string.toast_deleteOnlineRecipeFailed,Toast.LENGTH_LONG).show()
                }
            })
    }
}
