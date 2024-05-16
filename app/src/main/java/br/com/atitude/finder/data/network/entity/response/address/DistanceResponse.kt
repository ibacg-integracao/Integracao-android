package br.com.atitude.finder.data.network.entity.response.address

import com.google.gson.annotations.SerializedName

data class DistanceResponse(
    @SerializedName("distance") val distance: Double
)