package hu.bme.aut.android.cookbook.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import hu.bme.aut.android.cookbook.data.Recipe
import hu.bme.aut.android.cookbook.database.RecipeDAO
import hu.bme.aut.android.cookbook.database.RoomRecipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val recipeDAO: RecipeDAO) {

    fun getAllRecipes(): LiveData<List<Recipe>> {
        return recipeDAO.getAllRecipes()
            .map {roomRecipes ->
                roomRecipes.map {roomRecipe ->
                    roomRecipe.toDomainModel() }
            }
    }

    suspend fun insert(recipe: Recipe) = withContext(Dispatchers.IO) {
        recipeDAO.insertRecipe(recipe.toRoomModel())
    }
    suspend fun delete(recipe: Recipe) = withContext(Dispatchers.IO) {
        recipeDAO.deleteRecipe(recipe.toRoomModel())
    }
//    suspend fun deleteWithID(id: Int) = withContext(Dispatchers.IO) {
//        recipeDAO.deleteRecipeWithID(id)
//    }

    private fun RoomRecipe.toDomainModel(): Recipe {
        return Recipe(
            uID = uID,
            title = title,
            author = author,
            ingredients = ingredients,
            method = method,
            imageUrl = imageUrl,
            rating = rating
        )
    }

    private fun Recipe.toRoomModel(): RoomRecipe {  //TODO: maybe change Recipe elements to not be nullable
        return RoomRecipe(
            uID = uID!!,
            author = author!!,
            title = title!!,
            ingredients = ingredients!!,
            method = method!!,
            imageUrl =  imageUrl!!,
            rating =  rating!!
        )
    }
}