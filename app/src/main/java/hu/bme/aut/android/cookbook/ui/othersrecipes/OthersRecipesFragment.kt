package hu.bme.aut.android.cookbook.ui.othersrecipes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.cookbook.RecipesActivity
import hu.bme.aut.android.cookbook.adapter.RecipeAdapter
import hu.bme.aut.android.cookbook.data.Recipe
import hu.bme.aut.android.cookbook.databinding.FragmentOthersrecipesBinding
import hu.bme.aut.android.cookbook.ui.createrecipe.CreateRecipeFragment

class OthersRecipesFragment : Fragment() {

    private var _binding: FragmentOthersrecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeAdapter : RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOthersrecipesBinding.inflate(inflater, container, false)
        val root = binding.root

        recipeAdapter = RecipeAdapter(requireContext())
        binding.rvOthersRecipes.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.rvOthersRecipes.adapter = recipeAdapter

        initRecipesListener()

        binding.fabAddRecipe.setOnClickListener{
            (activity as RecipesActivity).addOnFragment(CreateRecipeFragment())
        }

        return root
    }

    private fun initRecipesListener() {
        val db = Firebase.firestore
        db.collection("recipes")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> recipeAdapter?.addRecipe(dc.document.toObject<Recipe>())
                        DocumentChange.Type.MODIFIED -> Toast.makeText(requireContext(), dc.document.data.toString(), Toast.LENGTH_SHORT).show()
                        DocumentChange.Type.REMOVED -> Toast.makeText(requireContext(), dc.document.data.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}