package hu.bme.aut.android.cookbook.ui.myrecipes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.cookbook.RecipesActivity
import hu.bme.aut.android.cookbook.adapter.RecipeAdapter
import hu.bme.aut.android.cookbook.data.Recipe
import hu.bme.aut.android.cookbook.databinding.FragmentMyrecipesBinding
import hu.bme.aut.android.cookbook.ui.createrecipe.CreateRecipeFragment

class MyRecipesFragment : Fragment() {

    private lateinit var currContext: Context
    private var _binding: FragmentMyrecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeAdapter : RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currContext = container!!.context
        _binding = FragmentMyrecipesBinding.inflate(inflater, container, false)
        val root = binding.root

        recipeAdapter = RecipeAdapter(currContext)
        binding.rvMyRecipes.layoutManager = GridLayoutManager(currContext,2, GridLayoutManager.VERTICAL, false)
//        binding.rvMyRecipes.layoutManager = LinearLayoutManager(currContext).apply {
//            reverseLayout = true
//            stackFromEnd = true
//        }
        binding.rvMyRecipes.adapter = recipeAdapter

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
                    Toast.makeText(currContext, e.toString(), Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> recipeAdapter?.addRecipe(dc.document.toObject<Recipe>())
                        DocumentChange.Type.MODIFIED -> Toast.makeText(currContext, dc.document.data.toString(), Toast.LENGTH_SHORT).show()
                        DocumentChange.Type.REMOVED -> Toast.makeText(currContext, dc.document.data.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    override fun onResume() {
        initRecipesListener()
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}