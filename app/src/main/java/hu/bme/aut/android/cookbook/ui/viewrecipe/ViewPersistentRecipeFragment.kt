package hu.bme.aut.android.cookbook.ui.viewrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import hu.bme.aut.android.cookbook.R
import hu.bme.aut.android.cookbook.data.Recipe
import hu.bme.aut.android.cookbook.databinding.FragmentViewpersistentrecipeBinding

class ViewPersistentRecipeFragment : Fragment() {

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

        return root
    }

    fun setUpView() {
        var requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)
        Glide.with(requireContext()).load(currRecipe.imageUrl).apply(requestOptions).into(binding.fragmentViewPersistentRecipeIvImage)

        binding.fragmentViewPersistentRecipeTvTitle.text = currRecipe.title
        binding.fragmentViewPersistentRecipeTvAuthor.text = currRecipe.author
        binding.fragmentViewPersistentRecipeTvRating.text = currRecipe.rating.toString()
        binding.fragmentViewPersistentRecipeTvIngredients.text = currRecipe.ingredients
        binding.fragmentViewPersistentRecipeTvMethod.text = currRecipe.method

        binding.fragmentViewPersistentRecipeBtnUpload.isVisible = (currRecipe.uID == "0")
    }

}