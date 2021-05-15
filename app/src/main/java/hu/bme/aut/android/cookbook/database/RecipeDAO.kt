package hu.bme.aut.android.cookbook.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecipeDAO {
    @Insert
    fun insertRecipe(recipe : RoomRecipe)

    @Query("SELECT * FROM recipe")
    fun getAllRecipes(): LiveData<List<RoomRecipe>>

    //Selective Get
//    @Query("SELECT * FROM recipe WHERE title LIKE :name")
//    fun getRecipeWithTitle(name: String):LiveData<List<RoomRecipe>>

    @Update
    fun updateRecipe(recipe : RoomRecipe): Int

    @Delete
    fun deleteRecipe(recipe : RoomRecipe)

}