package hu.bme.aut.android.cookbook.ui.othersrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hu.bme.aut.android.cookbook.R
import hu.bme.aut.android.cookbook.databinding.FragmentCreateRecipeBinding
import hu.bme.aut.android.cookbook.databinding.FragmentLoginBinding
import hu.bme.aut.android.cookbook.databinding.FragmentOthersrecipesBinding

class OthersRecipesFragment : Fragment() {

    private lateinit var othersRecipesViewModel: OthersRecipesViewModel
    private var _binding: FragmentOthersrecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        othersRecipesViewModel =
            ViewModelProvider(this).get(OthersRecipesViewModel::class.java)
        _binding = FragmentOthersrecipesBinding.inflate(inflater, container, false)
        val root = binding.root

        val textView: TextView = root.findViewById(R.id.tvOthersFragment)
        othersRecipesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // FONTOS!!!
    }
}