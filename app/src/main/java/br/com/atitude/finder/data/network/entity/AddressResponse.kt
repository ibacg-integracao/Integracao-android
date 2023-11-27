package br.com.atitude.finder.data.network.entity

import com.google.gson.annotations.SerializedName

data class AddressResponse(
    @SerializedName("address") val address: String
)