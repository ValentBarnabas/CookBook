package hu.bme.aut.android.cookbook.ui

import android.app.Activity
import android.content.Context
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
import hu.bme.aut.android.cookbook.Extensions.validateNonEmpty
import hu.bme.aut.android.cookbook.R
import hu.bme.aut.android.cookbook.data.Recipe
import hu.bme.aut.android.cookbook.databinding.FragmentCreateRecipeBinding
import hu.bme.aut.android.cookbook.databinding.FragmentMyrecipesBinding
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.util.*

class CreateRecipeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        private const val REQUEST_CODE = 101
    }

    private lateinit var currContext: Context
    private var _binding: FragmentCreateRecipeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        currContext = container!!.context
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
            uploadPost()
        } else {
            try {
                uploadPostWithImage()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun validateForm() = binding.etTitle.validateNonEmpty() && binding.etIngredients.validateNonEmpty() && binding.etMethod.validateNonEmpty()

    private fun uploadPost(imageUrl: String? = null) {
        val newRecipe = Recipe(FirebaseAuth.getInstance().currentUser.uid, FirebaseAuth.getInstance().currentUser.displayName, binding.etTitle.text.toString(),
            binding.etIngredients.text.toString(), binding.etMethod.text.toString(), imageUrl)

        val db = Firebase.firestore

        db.collection("recipes")
            .add(newRecipe)
            .addOnSuccessListener {
                Toast.makeText(currContext, currContext?.getString(R.string.create_recipe_success), Toast.LENGTH_LONG).show()
                //TODO: pop fragment stack
//                finish()
            }
            .addOnFailureListener { e -> Toast.makeText(currContext, e.toString(), Toast.LENGTH_LONG).show() }
    }

    private fun attachClick() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {   //TODO: ez nem activity, hanem fragment result
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

        val storageReference = FirebaseStorage.getInstance().reference
        val newImageName = URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8") + ".jpg"
        val newImageRef = storageReference.child("images/$newImageName")

        newImageRef.putBytes(imageInBytes)
            .addOnFailureListener { exception ->
                Toast.makeText(currContext, exception.message, Toast.LENGTH_LONG).show()
            }
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }

                newImageRef.downloadUrl
            }
            .addOnSuccessListener { downloadUri ->
                uploadPost(downloadUri.toString())
            }
    }

}