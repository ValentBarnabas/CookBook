package hu.bme.aut.android.cookbook.ui.viewrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.cookbook.R
import hu.bme.aut.android.cookbook.RecipesActivity
import hu.bme.aut.android.cookbook.data.Recipe
import hu.bme.aut.android.cookbook.databinding.FragmentViewfirebaserecipeBinding
import hu.bme.aut.android.cookbook.viewmodel.RecipeViewModel

class ViewFirebaseRecipeFragment : Fragment() {
    private var _binding: FragmentViewfirebaserecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var currRecipe: Recipe

    var likedAlready = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewfirebaserecipeBinding.inflate(inflater, container, false)
        val root = binding.root

        currRecipe = arguments?.getParcelable<Recipe>("extra")!!

        setUpView()

        return root
    }

    private fun setUpView() {
        var requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)
        Glide.with(requireContext()).load(currRecipe.imageUrl).apply(requestOptions).into(binding.fragmentViewFirebaseRecipeIvImage)

        binding.fragmentViewFirebaseRecipeTvTitle.text = currRecipe.title
        binding.fragmentViewFirebaseRecipeTvAuthor.text = currRecipe.author
        binding.fragmentViewFirebaseRecipeTvRating.text = currRecipe.rating.toString()
        binding.fragmentViewFirebaseRecipeTvIngredients.text = currRecipe.ingredients
        binding.fragmentViewFirebaseRecipeTvMethod.text = currRecipe.method

        binding.fragmentViewFirebaseRecipeFabLike.setOnClickListener {
            if(!likedAlready) {
                val newRating = currRecipe.rating + 1
                val updRecipe = Recipe(currRecipe.roomID, currRecipe.firebaseID, currRecipe.author, currRecipe.title, currRecipe.ingredients, currRecipe.method, currRecipe.imageUrl, newRating)

                RecipeViewModel().update(updRecipe)

                val docRef = Firebase.firestore.collection("recipes").document(currRecipe.firebaseID.toString())
                Firebase.firestore.runTransaction {
                    val snapshot = it.get(docRef)
                    val newRating = (snapshot.getDouble("rating")!! + 1).toInt()
                    it.update(docRef, "rating", newRating)
                }.addOnSuccessListener {
                    likedAlready = true
                    if(context != null) Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
                    binding.fragmentViewFirebaseRecipeTvRating.text = newRating.toString()
                }.addOnFailureListener{
                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
                    likedAlready = false
                }
                likedAlready = true
            } else {
                Toast.makeText(requireContext(), "Please only like a recipe once", Toast.LENGTH_SHORT).show()
            }
        }
    }

}