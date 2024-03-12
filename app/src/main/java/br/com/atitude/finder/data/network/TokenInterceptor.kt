package br.com.atitude.finder.data.network

import br.com.atitude.finder.repository.SharedPrefs
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val sharedPrefs: SharedPrefs): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPrefs.getToken().takeIf { it.isNotBlank() } ?: return chain.proceed(chain.request())
        val request = chain.request()
        val newRequest = request
            .newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}