package hu.bme.aut.android.cookbook.ui.viewrecipe

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.cookbook.R
import hu.bme.aut.android.cookbook.RecipesActivity
import hu.bme.aut.android.cookbook.data.Recipe
import hu.bme.aut.android.cookbook.databinding.FragmentViewfirebaserecipeBinding
import hu.bme.aut.android.cookbook.notification.*
import hu.bme.aut.android.cookbook.viewmodel.RecipeViewModel

class ViewFirebaseRecipeFragment : Fragment() {
    private var _binding: FragmentViewfirebaserecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var currRecipe: Recipe
    private val likeThreshold : Int = 50

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
        binding.fragmentViewFirebaseRecipeTvAuthor.text = getString(R.string.fragment_viewANYRecipe_RecipeBy) + " " + currRecipe.author
        binding.fragmentViewFirebaseRecipeTvRating.text = " " + currRecipe.rating.toString()
        binding.fragmentViewFirebaseRecipeTvIngredients.text = currRecipe.ingredients
        binding.fragmentViewFirebaseRecipeTvMethod.text = currRecipe.method

        binding.fragmentViewFirebaseRecipeFabLike.setOnClickListener {
            if(!likedAlready) {
                val newRating = currRecipe.rating?.plus(1)
                val docRef = Firebase.firestore.collection("recipes").document(currRecipe.firebaseID.toString())
                Firebase.firestore.runTransaction {
                    val snapshot = it.get(docRef)
                    val newRating = (snapshot.getDouble("rating")!! + 1).toInt()
                    it.update(docRef, "rating", newRating)
                }.addOnSuccessListener {
                    if(context != null){
                        likedAlready = true
                        binding.fragmentViewFirebaseRecipeTvRating.text = " " + newRating.toString()
                        binding.fragmentViewFirebaseRecipeFabLike.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.grey_font))

                        sendLikeMessage()
                        if(newRating == likeThreshold) sendTryRecipe()
                    }


                }.addOnFailureListener{
                    if(context != null) Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
                    likedAlready = false
                }
                likedAlready = true
            } else {
                if(context != null) Toast.makeText(requireContext(), R.string.fragment_viewFirebaserecipe_rateMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendLikeMessage() {
        val title = getString(R.string.popup_notif_like_title)
        val message: String
        if(FirebaseAuth.getInstance().currentUser != null) {
            val currUser = FirebaseAuth.getInstance().currentUser.displayName
            message = "$currUser " + getString(R.string.popup_notif_line_login_message)
        } else {
            message = getString(R.string.popup_notif_like_anon_message)
        }

        if(title != null && message != null && currRecipe.authorID != null) {
            FirebaseService.token = currRecipe.authorID
            PushNotification(
                NotificationData(title, message),
                currRecipe.authorID.toString()
            ).also {
                sendNotification(it)
            }
        }
    }

    private fun sendTryRecipe() {
        val title = getString(R.string.popup_notif_recommend_recipe_title)
        val message = currRecipe.title + " " + getString(R.string.popup_notif_recommend_recipe_message_1) + " " + currRecipe.author + " " + getString(R.string.popup_notif_recommend_recipe_message_2) + " " + likeThreshold.toString() + " " + getString(R.string.popup_notif_recommend_recipe_message_3)

        if(title != null && message != null) {
            PushNotification(
                NotificationData(title, message),
                TOPIC
            ).also {
                sendNotification(it)
            }
        }
    }
}