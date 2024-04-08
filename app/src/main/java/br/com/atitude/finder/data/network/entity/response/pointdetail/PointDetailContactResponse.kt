package br.com.atitude.finder.data.network.entity.response.pointdetail

import br.com.atitude.finder.domain.Gender
import br.com.atitude.finder.domain.pointdetail.PointDetailContact
import com.google.gson.annotations.SerializedName

data class PointDetailContactResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("contact_phone") val contactPhone: String,
    @SerializedName("gender") val gender: Char
)

fun PointDetailContactResponse.toDomain() =
    PointDetailContact(
        id = this.id,
        name = this.name,
        contactPhone = this.contactPhone,
        gender = Gender.getByLetter(this.gender)
    )