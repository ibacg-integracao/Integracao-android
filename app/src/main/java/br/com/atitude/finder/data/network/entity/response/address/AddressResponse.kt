package br.com.atitude.finder.data.network.entity.response.address

import com.google.gson.annotations.SerializedName

data class AddressResponse(
    @SerializedName("address") val address: String
)