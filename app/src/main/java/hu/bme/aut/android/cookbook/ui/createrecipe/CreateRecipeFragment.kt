package hu.bme.aut.android.cookbook.ui.createrecipe

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.installations.FirebaseInstallations
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

class CreateRecipeFragment : Fragment() {

    companion object {
        private const val REQUEST_CODE_TAKE = 101
        private const val REQUEST_CODE_PICK = 102
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
        binding.btnImageFromGallery.setOnClickListener { galleryClick() }
        binding.btnImageFromCamera.setOnClickListener { cameraClick() }

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

    private fun uploadPostWithImage() {
        val bitmap: Bitmap = (binding.ivImage.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageInBytes = baos.toByteArray()

        if(isOnline(requireContext()) && FirebaseAuth.getInstance().currentUser != null) {                    //User has internet access, we can get downloadURI
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
                    val downloadURI = downloadUri

                    FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener{
//                        FirebaseService.token = it.result!!.token
                        val authorToken = it.result!!.token

                        val db = Firebase.firestore
                        val fbDocRef = db.collection("recipes").document()
                        var newRecipe: Recipe
                        if(FirebaseAuth.getInstance().currentUser != null) {        //If use is logged in, will use his name as author
                            newRecipe = Recipe(0, fbDocRef.id, FirebaseAuth.getInstance().currentUser.displayName, authorToken, binding.etTitle.text.toString(), binding.etIngredients.text.toString(),
                                binding.etMethod.text.toString(), downloadUri.toString(), 0)
                        } else {                                                    //Else we use Anonymous
                            newRecipe = Recipe(0, fbDocRef.id, "Anonymous", "0", binding.etTitle.text.toString(), binding.etIngredients.text.toString(),
                                binding.etMethod.text.toString(), downloadUri.toString(), 0)
                        }

                        RecipeViewModel().insert(newRecipe)                //Now we add item to Room database to get item ID
                        fbDocRef.set(newRecipe)
                            .addOnSuccessListener {
                                if(context != null) Toast.makeText(requireContext(), requireContext()?.getString(R.string.create_recipe_success), Toast.LENGTH_LONG).show()
                                val fragMan = activity?.supportFragmentManager
                                fragMan?.popBackStack()
                                (activity as RecipesActivity).swapToFragment(MyRecipesFragment())
                            }
                            .addOnFailureListener { e -> Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show() }
                    }
                }
        } else {            //user doesnt have internet access, cant get downloadURI, or not logged in
            //TODO: save on persistent storage and send toast about not being uploaded online -> offline mentesnel a file-t elmentjuk, es csak utvonalat tarolunk hozza, firebaseID ="0"
            Toast.makeText(requireContext(), getString(R.string.create_recipe_illegal_upload), Toast.LENGTH_LONG).show()
        }
    }

    private fun cameraClick() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent,
            REQUEST_CODE_TAKE
        )
    }
    private fun galleryClick() {
        val pickPictureIntent = Intent()
        pickPictureIntent.type = "image/*"
        pickPictureIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(pickPictureIntent, REQUEST_CODE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {   //Handles camera response, and shows image in imageView
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_TAKE) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap ?: return
            binding.ivImage.setImageBitmap(imageBitmap)
            binding.ivImage.visibility = View.VISIBLE
        }

        else if (requestCode == REQUEST_CODE_PICK && data != null && data.data != null) {
            val imageUri = data.data
            binding.ivImage.setImageURI(imageUri)
            binding.ivImage.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}