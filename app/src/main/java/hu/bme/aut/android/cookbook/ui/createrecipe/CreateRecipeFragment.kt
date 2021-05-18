package hu.bme.aut.android.cookbook.ui.createrecipe

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import hu.bme.aut.android.cookbook.Extensions.isOnline
import hu.bme.aut.android.cookbook.Extensions.validateNonEmpty
import hu.bme.aut.android.cookbook.R
import hu.bme.aut.android.cookbook.RecipesActivity
import hu.bme.aut.android.cookbook.data.Recipe
import hu.bme.aut.android.cookbook.databinding.FragmentCreateRecipeBinding
import hu.bme.aut.android.cookbook.ui.myrecipes.MyRecipesFragment
import hu.bme.aut.android.cookbook.viewmodel.RecipeViewModel
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.util.*


//TODO: edit, hogy lehessen ugy is hozzaadni, hogy kivalasztjuk, csak magunknak akarjuk, csak masoknak, vagy mindketto
class CreateRecipeFragment : Fragment() {

    companion object {
        private const val REQUEST_CODE = 101
    }

    private var _binding: FragmentCreateRecipeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        _binding = FragmentCreateRecipeBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.btnSend.setOnClickListener { sendClick() }
        binding.btnSetImage.setOnClickListener { attachClick() }

        return root
    }

    private fun sendClick() {
        if (!validateForm()) {
            return
        }

        if (binding.ivImage.visibility != View.VISIBLE) {
            if(context != null) Toast.makeText(requireContext(), requireContext()?.getString(R.string.create_recipe_add_image), Toast.LENGTH_LONG).show()
        } else {
            try {
                uploadPostWithImage()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun validateForm() = binding.etTitle.validateNonEmpty() && binding.etIngredients.validateNonEmpty() && binding.etMethod.validateNonEmpty()

    private fun uploadRecipe(imageUrl: String? = null) : String{
        val newRecipe = Recipe(FirebaseAuth.getInstance().currentUser.uid, FirebaseAuth.getInstance().currentUser.displayName, binding.etTitle.text.toString(),
            binding.etIngredients.text.toString(), binding.etMethod.text.toString(), imageUrl, 0)

        val db = Firebase.firestore

        val docRef = db.collection("recipes").document()
        docRef.set(newRecipe)
            .addOnSuccessListener {
                if(context != null) Toast.makeText(requireContext(), requireContext()?.getString(R.string.create_recipe_success), Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e -> Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show() }
        return docRef.id
    }

    private fun saveRecipe(imageUrl: String? = null, uID : String? = null) {
        val newRecipe:Recipe
        if(isOnline(requireContext()) && FirebaseAuth.getInstance().currentUser != null){                                     //User is online and logged in -> can get uID and author, will use those values
            newRecipe = Recipe(uID, FirebaseAuth.getInstance().currentUser.displayName, binding.etTitle.text.toString(),
                binding.etIngredients.text.toString(), binding.etMethod.text.toString(), imageUrl, 0)
        } else {                                                                                                              //User is offline or nor logged in -> cant get uID and author, so it will be 0 and Anonymous
            newRecipe = Recipe("0", "Anonymous", binding.etTitle.text.toString(),
                binding.etIngredients.text.toString(), binding.etMethod.text.toString(), imageUrl, 0)
        }

        val dbVM = RecipeViewModel()
        dbVM.insert(newRecipe)
    }

    private fun attachClick() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent,
            REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {   //Handles camera response, and shows image in imageView
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap ?: return
            binding.ivImage.setImageBitmap(imageBitmap)
            binding.ivImage.visibility = View.VISIBLE
        }
    }

    private fun uploadPostWithImage() {
        val bitmap: Bitmap = (binding.ivImage.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageInBytes = baos.toByteArray()

        if(isOnline(requireContext())) {
            val storageReference = FirebaseStorage.getInstance().reference
            val newImageName = URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8") + ".jpg"
            val newImageRef = storageReference.child("images/$newImageName")

            newImageRef.putBytes(imageInBytes)
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), exception.message, Toast.LENGTH_LONG).show()
                }
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }

                    newImageRef.downloadUrl
                }
                .addOnSuccessListener { downloadUri ->
                    var uID : String = uploadRecipe(downloadUri.toString())
                    saveRecipe(downloadUri.toString(), uID)
                    Toast.makeText(requireContext(), requireContext()?.getString(R.string.create_recipe_success), Toast.LENGTH_LONG).show()
                    val fragMan = activity?.supportFragmentManager
                    fragMan?.popBackStack()
                    (activity as RecipesActivity).swapToFragment(MyRecipesFragment())
                }
        } else {
            //TODO: save on persistent storage and send toast about not being uploaded online -> question: ha downloadUri-kat mentunk el, akkor azt hogyan szerzem meg offline?
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}