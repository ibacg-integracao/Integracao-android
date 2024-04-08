package br.com.atitude.finder.data.network.entity.request

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("old_password") val oldPassword: String,
    @SerializedName("password") val password: String
)