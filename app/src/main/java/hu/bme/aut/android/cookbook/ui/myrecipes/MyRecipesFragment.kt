package hu.bme.aut.android.cookbook.ui.myrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import hu.bme.aut.android.cookbook.RecipesActivity
import hu.bme.aut.android.cookbook.adapter.PersistentRecipeAdapter
import hu.bme.aut.android.cookbook.data.Recipe
import hu.bme.aut.android.cookbook.databinding.FragmentMyrecipesBinding
import hu.bme.aut.android.cookbook.ui.createrecipe.CreateRecipeFragment
import hu.bme.aut.android.cookbook.ui.viewrecipe.ViewPersistentRecipeFragment
import hu.bme.aut.android.cookbook.viewmodel.RecipeViewModel

class MyRecipesFragment : Fragment(), PersistentRecipeAdapter.OnItemClickListener {

    private var _binding: FragmentMyrecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeViewModel: RecipeViewModel

    private lateinit var recipeAdapter : PersistentRecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyrecipesBinding.inflate(inflater, container, false)
        val root = binding.root

        recipeAdapter = PersistentRecipeAdapter(requireContext())
        recipeAdapter.itemClickListener = this


        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
        recipeViewModel.allRecipes.observe(requireActivity()) { recipes ->
            recipeAdapter.submitList(recipes)
        }

        binding.rvMyRecipes.layoutManager = StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL)
        binding.rvMyRecipes.adapter = recipeAdapter

        binding.fabAddRecipe.setOnClickListener{
            (activity as RecipesActivity).addOnFragment(CreateRecipeFragment())
        }

        return root
    }

    override fun onItemClick(recipe: Recipe) {      //give current recipe to the fragment -> addOnFragmentWithInfo(recipe)
        (activity as RecipesActivity).addOnFragmentWithExtra(ViewPersistentRecipeFragment(), recipe)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}