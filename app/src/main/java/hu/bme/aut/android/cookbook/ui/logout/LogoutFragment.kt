package hu.bme.aut.android.cookbook.ui.logout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.rpc.context.AttributeContext
import hu.bme.aut.android.cookbook.R
import hu.bme.aut.android.cookbook.RecipesActivity
import hu.bme.aut.android.cookbook.databinding.FragmentLogoutBinding

class LogoutFragment : Fragment() {

    private lateinit var logoutViewModel: LogoutViewModel
    private var _binding: FragmentLogoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logoutViewModel =
            ViewModelProvider(this).get(LogoutViewModel::class.java)
        _binding = FragmentLogoutBinding.inflate(inflater, container, false)
        val root = binding.root

        val textView: TextView = root.findViewById(R.id.text_logout)
        logoutViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        FirebaseAuth.getInstance().signOut()
        (activity as RecipesActivity).updateDrawerInformation()
        if(context != null) Toast.makeText(requireContext(), R.string.authentication_logout_successfull, Toast.LENGTH_LONG).show()
        (activity as RecipesActivity).startActivity(Intent(activity, RecipesActivity::class.java))

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}