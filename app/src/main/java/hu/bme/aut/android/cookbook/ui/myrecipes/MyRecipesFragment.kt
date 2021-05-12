package hu.bme.aut.android.cookbook.ui.myrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hu.bme.aut.android.cookbook.R

class MyRecipesFragment : Fragment() {

    private lateinit var myRecipesViewModel: MyRecipesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myRecipesViewModel =  ViewModelProvider(this).get(MyRecipesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_myrecipes, container, false)
        val textView: TextView = root.findViewById(R.id.tvOwnFragment)
        myRecipesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}