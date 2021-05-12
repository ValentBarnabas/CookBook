package hu.bme.aut.android.cookbook.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

private val _text = MutableLiveData<String>().apply {
    value = "This is login Fragment"
}
val text: LiveData<String> = _text