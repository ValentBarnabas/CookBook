package hu.bme.aut.android.cookbook.ui.viewrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import hu.bme.aut.android.cookbook.R
import hu.bme.aut.android.cookbook.data.Recipe
import hu.bme.aut.android.cookbook.databinding.FragmentViewfirebaserecipeBinding

class ViewFirebaseRecipeFragment : Fragment() {
    private var _binding: FragmentViewfirebaserecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var currRecipe: Recipe

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
    }

}