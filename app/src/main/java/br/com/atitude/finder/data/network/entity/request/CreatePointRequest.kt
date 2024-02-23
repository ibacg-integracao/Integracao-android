package br.com.atitude.finder.data.network.entity.request

import com.google.gson.annotations.SerializedName

data class CreatePointRequest(
    @SerializedName("name") val name: String,
    @SerializedName("street") val street: String,
    @SerializedName("neighborhood") val neighborhood: String,
    @SerializedName("state") val state: String,
    @SerializedName("city") val city: String,
    @SerializedName("complement") val complement: String?,
    @SerializedName("number") val number: Int?,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("postal_code") val postalCode: String,
    @SerializedName("leader_name") val leaderName: String,
    @SerializedName("tag") val tag: String,
    @SerializedName("hour") val hour: Int,
    @SerializedName("minutes") val minutes: Int,
    @SerializedName("week_day") val weekDay: String,
    @SerializedName("sector_id") val sectorId: String,
    @SerializedName("phone_contacts") val phoneContacts: List<CreatePointContactRequest>,
    @SerializedName("reference") val reference: String?,
)