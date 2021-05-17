package hu.bme.aut.android.cookbook.data

import android.os.Parcel
import android.os.Parcelable

class Recipe (
    var uID: String? = null,
    var author: String? = null,
    var title: String? = null,
    var ingredients: String? = null,
    var method: String? = null,
    var imageUrl: String? = null,
    var rating: Int? = null
//    var comments: String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uID)
        parcel.writeString(author)
        parcel.writeString(title)
        parcel.writeString(ingredients)
        parcel.writeString(method)
        parcel.writeString(imageUrl)
        parcel.writeValue(rating)
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