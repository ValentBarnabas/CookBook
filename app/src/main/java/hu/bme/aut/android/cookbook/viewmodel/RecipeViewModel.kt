package hu.bme.aut.android.cookbook.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.cookbook.RecipeApplication
import hu.bme.aut.android.cookbook.data.Recipe
import hu.bme.aut.android.cookbook.repository.Repository
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {
    private val repository: Repository

    val allRecipes: LiveData<List<Recipe>>

    init {
        val recipeDAO = RecipeApplication.recipeDatabase.recipeDAO()
        repository = Repository(recipeDAO)
        allRecipes = repository.getAllRecipes()
    }

    fun insert(recipe: Recipe) = viewModelScope.launch {
        repository.insert(recipe)
    }
}