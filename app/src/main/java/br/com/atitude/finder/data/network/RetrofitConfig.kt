package br.com.atitude.finder.data.network

import br.com.atitude.finder.BuildConfig
import br.com.atitude.finder.repository.SharedPrefs
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConfig {
    companion object {
        private fun client(sharedPrefs: SharedPrefs): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(TokenInterceptor(sharedPrefs))
                .build()
        }

        fun instantiate(sharedPrefs: SharedPrefs): Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(client(sharedPrefs))
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        .create()
                )
            )
            .build()
    }
}