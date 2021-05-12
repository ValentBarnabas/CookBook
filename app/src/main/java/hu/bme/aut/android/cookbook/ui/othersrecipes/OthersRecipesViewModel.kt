package hu.bme.aut.android.cookbook.ui.othersrecipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OthersRecipesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is othersrecipes Fragment"
    }
    val text: LiveData<String> = _text
}