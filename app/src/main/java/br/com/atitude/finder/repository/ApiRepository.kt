package br.com.atitude.finder.repository

import br.com.atitude.finder.domain.SimplePoint
import br.com.atitude.finder.domain.PointTime
import br.com.atitude.finder.domain.PostalCodeAddressInfo
import br.com.atitude.finder.domain.SearchParams
import br.com.atitude.finder.domain.Sector
import br.com.atitude.finder.domain.WeekDay

interface ApiRepository {

    suspend fun deletePoint(id: String)

    suspend fun getAllPoints(): List<SimplePoint>

    suspend fun getWeekDays(): List<WeekDay>
    suspend fun searchPoints(
        postalCode: String,
        weekDays: List<String>,
        tags: List<String>,
        times: List<String>
    ): List<SimplePoint>

    suspend fun getPointsTime(): List<PointTime>
    suspend fun searchParams(): SearchParams
    suspend fun createPoint(
        name: String,
        street: String,
        neighborhood: String,
        state: String,
        city: String,
        complement: String?,
        latitude: Double,
        longitude: Double,
        postalCode: String,
        number: Int?,
        leaderName: String,
        leaderPhone: String,
        tag: String,
        hour: Int,
        minutes: Int,
        weekDay: String,
        sectorId: String
    )

    suspend fun getPostalCodeAddress(postalCode: String): PostalCodeAddressInfo?

    suspend fun getAllSectors(): List<Sector>
}