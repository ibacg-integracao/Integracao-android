package br.com.atitude.finder.data.network.entity.request

import com.google.gson.annotations.SerializedName

data class RegisterAccountRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)