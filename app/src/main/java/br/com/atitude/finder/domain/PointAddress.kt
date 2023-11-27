package br.com.atitude.finder.domain

import com.google.android.gms.maps.model.LatLng

data class PointAddress(
    val address: String,
    val coordinates: LatLng,
    val postalCode: String
)