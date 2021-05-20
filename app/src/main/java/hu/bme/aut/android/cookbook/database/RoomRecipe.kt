package hu.bme.aut.android.cookbook.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes_table")   //Declares that the next class will be an entity in said table
data class RoomRecipe(  //Reprezents a Recipe item in the room database
    @PrimaryKey(autoGenerate = true)    //automatically generates primary key
    var roomID: Int = 0,    //ID that Room will generate for the item
    var firebaseID: String,    //Firebase uID of item. If it is 0, then recipe has not yet been uploaded to firebase
    var author: String,
    var authorID: String,       //FirebaseInstallation token for sending messages
    var title: String,
    var ingredients: String,
    var method: String,
    var imageUrl: String,
    var rating: Int
)

