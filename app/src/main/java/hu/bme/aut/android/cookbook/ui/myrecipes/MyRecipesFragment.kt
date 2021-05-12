package hu.bme.aut.android.cookbook.ui.myrecipes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.cookbook.R
import hu.bme.aut.android.cookbook.adapter.RecipeAdapter
import hu.bme.aut.android.cookbook.data.Recipe
import hu.bme.aut.android.cookbook.databinding.FragmentMyrecipesBinding

class MyRecipesFragment : Fragment() {

//    private lateinit var myRecipesViewModel: MyRecipesViewModel
//    private lateinit var recipeAdapter: RecipeAdapter

    private lateinit var currContext: Context
    private var _binding: FragmentMyrecipesBinding? = null
    private val binding get() = _binding!!

    private var recyclerView : RecyclerView? = null
    private var gridLayoutManager : GridLayoutManager? = null
    private var arrayList : ArrayList<Recipe>? = null
    private var recipeAdapter: RecipeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currContext = container!!.context
        _binding = FragmentMyrecipesBinding.inflate(inflater, container, false)
        val root = binding.root

//        myRecipesViewModel =  ViewModelProvider(this).get(MyRecipesViewModel::class.java)

        recyclerView = root.findViewById(R.id.rvMyRecipes)
        gridLayoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        arrayList = ArrayList()
//        arrayList = setDataInList()
        recipeAdapter = RecipeAdapter(currContext)
        recyclerView?.adapter = recipeAdapter

        initPostsListener()

//        recipeAdapter = RecipeAdapter(currContext)
//        binding.appBarRecipes.contentRecipes.rvRecipes.layoutManager = LinearLayoutManager(currContext).apply {
//            stackFromEnd = true
//        }
//        binding.appBarRecipes.contentRecipes.rvRecipes.adapter = recipeAdapter


        return root
    }

//    private fun setDataInList() : ArrayList<Recipe> {
//
//    }

    private fun initPostsListener() {
        val db = Firebase.firestore
        db.collection("posts")
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
}