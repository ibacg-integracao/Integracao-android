package br.com.atitude.finder.data.network.entity.request

import com.google.gson.annotations.SerializedName

data class UpdatePointRequest(
    @SerializedName("state") val state: String?
)