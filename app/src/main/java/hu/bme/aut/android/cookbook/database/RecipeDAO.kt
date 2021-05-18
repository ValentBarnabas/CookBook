package hu.bme.aut.android.cookbook.database

import androidx.lifecycle.LiveData
import androidx.room.*
import hu.bme.aut.android.cookbook.R


@Dao
interface RecipeDAO {
    @Insert
    fun insertRecipe(recipe : RoomRecipe)

    @Query("SELECT * FROM recipes_table")
    fun getAllRecipes(): LiveData<List<RoomRecipe>>

    //Selective Get
//    @Query("SELECT * FROM recipe WHERE author LIKE :author")
//    fun getRecipeByAuthor(author: String):LiveData<List<RoomRecipe>>

    @Update
    fun updateRecipe(recipe : RoomRecipe)

    @Delete
    fun deleteRecipe(recipe : RoomRecipe)

    //Selective delete
//    @Query("DELETE FROM recipes_table WHERE id = :id")
//    fun deleteRecipeWithID(id: Int)
}