package hu.bme.aut.android.cookbook.data

import android.os.Parcel
import android.os.Parcelable

class Recipe(
    var roomID: Int = 0,
    var firebaseID: String? = null,
    var author: String? = null,
    var title: String? = null,
    var ingredients: String? = null,
    var method: String? = null,
    var imageUrl: String? = null,
    var rating: Int? = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(roomID)
        parcel.writeString(firebaseID)
        parcel.writeString(author)
        parcel.writeString(title)
        parcel.writeString(ingredients)
        parcel.writeString(method)
        parcel.writeString(imageUrl)
        parcel.writeInt(rating!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Recipe> {
        override fun createFromParcel(parcel: Parcel): Recipe {
            return Recipe(parcel)
        }

        override fun newArray(size: Int): Array<Recipe?> {
            return arrayOfNulls(size)
        }
    }
}