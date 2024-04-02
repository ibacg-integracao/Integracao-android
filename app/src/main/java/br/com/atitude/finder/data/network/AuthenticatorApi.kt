package br.com.atitude.finder.data.network

import br.com.atitude.finder.data.network.entity.AuthenticatedUserResponse
import br.com.atitude.finder.data.network.entity.TokenResponse
import br.com.atitude.finder.data.network.entity.request.LoginRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthenticatorApi {
    @POST("v1/auth/login")
    fun login(@Body request: LoginRequest): Call<TokenResponse>
}