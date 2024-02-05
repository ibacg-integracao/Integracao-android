package br.com.atitude.finder.data.network.entity.request

import com.google.gson.annotations.SerializedName

data class CreatePointContactRequest(
    @SerializedName("name") val name: String,
    @SerializedName("gender") val gender: Char,
    @SerializedName("contact_phone") val phone: String
)