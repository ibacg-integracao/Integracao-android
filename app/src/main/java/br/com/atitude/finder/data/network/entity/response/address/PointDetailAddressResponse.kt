package br.com.atitude.finder.data.network.entity.response.address

import com.google.gson.annotations.SerializedName

data class PointDetailAddressResponse(
    @SerializedName("address") val address: String,
    @SerializedName("postal_code") val postalCode: String,
)