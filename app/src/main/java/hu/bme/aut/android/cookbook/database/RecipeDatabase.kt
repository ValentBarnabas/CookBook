package hu.bme.aut.android.cookbook.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    exportSchema = false,
    entities = [RoomRecipe::class]
)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDAO(): RecipeDAO
}