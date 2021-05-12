package hu.bme.aut.android.cookbook.ui.myrecipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyRecipesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is myrecipes Fragment"
    }
    val text: LiveData<String> = _text
}