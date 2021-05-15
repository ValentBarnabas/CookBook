package hu.bme.aut.android.cookbook.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")   //Declares that the next class will be an entity
data class RoomRecipe(  //Reprezents a Recipe item in the room database
    @PrimaryKey(autoGenerate = true)    //automatically generates primary key
    var id: Int = 0,    //ID that Room will generate for the item
    var uID: String,    //Firebase uID of item
    var author: String,
    var title: String,
    var ingredients: String,
    var method: String,
    var imageUrl: String,
    var rating: Int
)

