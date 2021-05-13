package hu.bme.aut.android.cookbook.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import hu.bme.aut.android.cookbook.Extensions.validateNonEmpty
import hu.bme.aut.android.cookbook.R
import hu.bme.aut.android.cookbook.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var currContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        currContext = container!!.getContext()
        firebaseAuth = FirebaseAuth.getInstance()
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.btnRegister.setOnClickListener { registerClick() }
        binding.btnLogin.setOnClickListener { loginClick() }

        return root
    }

    private fun validateForm() = binding.etEmail.validateNonEmpty() && binding.etPassword.validateNonEmpty()

    private fun registerClick() {
        if (!validateForm()) { return }

        firebaseAuth
            .createUserWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            .addOnSuccessListener { result ->
                val firebaseUser = result.user
                val profileChangeRequest = UserProfileChangeRequest.Builder()
                    .setDisplayName(firebaseUser?.email?.substringBefore('@'))
                    .build()
                firebaseUser?.updateProfile(profileChangeRequest)

                Toast.makeText(currContext, context?.getString(R.string.authentication_registration_successfull), Toast.LENGTH_LONG).show()
            }

            .addOnFailureListener { exception -> Toast.makeText(currContext, exception.message, Toast.LENGTH_LONG).show() }
    }

    private fun loginClick() {
        if (!validateForm()) { return }

        firebaseAuth
            .signInWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            .addOnSuccessListener {
                Toast.makeText(currContext, context?.getString(R.string.authentication_login_successfull), Toast.LENGTH_LONG).show()
                fragmentManager?.popBackStackImmediate();       //TODO: itt nem popBackStackImmediate(), hanem becsukni ezt, es visszamenni a sajat receptekbe
//                        fragmentManager?.beginTransaction()?.apply {
//                                replace(, MyRecipesFragment())
//                                commit()
//                        }
            }
            .addOnFailureListener { exception -> Toast.makeText(currContext, exception.message, Toast.LENGTH_LONG).show()}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}