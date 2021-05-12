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

class OthersRecipesFragment : Fragment() {

    private lateinit var othersRecipesViewModel: OthersRecipesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        othersRecipesViewModel =
            ViewModelProvider(this).get(OthersRecipesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_othersrecipes, container, false)
        val textView: TextView = root.findViewById(R.id.tvOthersFragment)
        othersRecipesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}