package hu.bme.aut.android.cookbook

import android.app.Application
import androidx.room.Room
import hu.bme.aut.android.cookbook.database.RecipeDatabase

class RecipeApplication : Application() {

    companion object {
        lateinit var recipeDatabase: RecipeDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        recipeDatabase = Room.databaseBuilder(      //Could use InMemoryDatabaseBuilder, in order for testing, that way it only stores data in memory
            applicationContext,
            RecipeDatabase::class.java,
            "recipe_database"
        ).fallbackToDestructiveMigration().build()  //use .addMigrations() to define how it should change the database. In our case, fallback is good enough
    }

}