package br.com.atitude.finder.data.network.entity

import br.com.atitude.finder.domain.PostalCodeAddressInfo
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

data class PostalCodeAddressInfoResponse(
    @SerializedName("postal_code") val postalCode: String,
    @SerializedName("street") val street: String?,
    @SerializedName("neighborhood") val neighborhood: String?,
    @SerializedName("state") val state: String,
    @SerializedName("city") val city: String,
    @SerializedName("country") val country: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,

    )

fun PostalCodeAddressInfoResponse.toDomain(): PostalCodeAddressInfo = PostalCodeAddressInfo(
    postalCode = this.postalCode,
    street = this.street,
    neighborhood = this.neighborhood,
    state = this.state,
    city = this.city,
    country = this.country,
    coordinates = LatLng(this.latitude, this.longitude)
)