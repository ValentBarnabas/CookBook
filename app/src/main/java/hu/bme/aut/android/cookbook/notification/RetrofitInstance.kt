package hu.bme.aut.android.cookbook.notification

import hu.bme.aut.android.cookbook.notification.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val API by lazy {
            retrofit.create(NotificationAPI::class.java)
        }
    }

}