package br.com.atitude.finder.domain

import com.google.android.gms.maps.model.LatLng

data class PostalCodeAddressInfo(
    val postalCode: String,
    val street: String?,
    val neighborhood: String?,
    val state: String,
    val city: String,
    val country: String,
    val coordinates: LatLng
)