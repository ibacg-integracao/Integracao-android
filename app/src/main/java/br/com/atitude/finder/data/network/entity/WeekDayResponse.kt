package br.com.atitude.finder.data.network.entity

import com.google.gson.annotations.SerializedName

data class WeekDayResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("business_day") val businessDay: Boolean,
)