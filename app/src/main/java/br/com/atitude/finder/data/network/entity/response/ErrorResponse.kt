package br.com.atitude.finder.data.network.entity.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("message") val message: String
)