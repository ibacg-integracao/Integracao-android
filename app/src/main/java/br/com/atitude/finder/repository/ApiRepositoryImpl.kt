package br.com.atitude.finder.repository

import br.com.atitude.finder.data.network.NetworkApi
import br.com.atitude.finder.data.network.entity.request.CreatePointRequest
import br.com.atitude.finder.data.network.entity.toDomain
import br.com.atitude.finder.domain.PostalCodeAddressInfo
import br.com.atitude.finder.domain.SearchParams
import br.com.atitude.finder.domain.Sector
import br.com.atitude.finder.domain.SimplePoint
import br.com.atitude.finder.domain.WeekDay
import br.com.atitude.finder.extensions.toPointTime

class ApiRepositoryImpl(private val networkApi: NetworkApi) : ApiRepository {
    override suspend fun deletePoint(id: String) {
        networkApi.deletePoint(id)
    }

    override suspend fun getAllPoints(): List<SimplePoint> =
        networkApi.getAllPoints().map { it.toDomain() }

    override suspend fun getWeekDays(): List<WeekDay> =
        networkApi.getWeekDays().mapNotNull { WeekDay.getByResponse(it.name) }

    override suspend fun searchPoints(
        postalCode: String,
        weekDays: List<String>,
        tags: List<String>,
        times: List<String>
    ): List<SimplePoint> {
        return networkApi.searchPoints(
            postalCode = postalCode,
            weekDays = weekDays,
            tags = tags,
            times = times
        )
            .map { it.toDomain() }
    }

    override suspend fun getPointsTime() = networkApi.pointsTime().map { it.toPointTime() }

    override suspend fun searchParams(): SearchParams = networkApi.searchParams().toDomain()
    override suspend fun createPoint(
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
    ) {
        networkApi.createPoint(
            CreatePointRequest(
                name = name,
                street = street,
                leaderName = leaderName,
                leaderPhone = leaderPhone,
                latitude = latitude,
                longitude = longitude,
                postalCode = postalCode,
                number = number,
                tag = tag,
                hour = hour,
                minutes = minutes,
                weekDay = weekDay,
                city = city,
                state = state,
                complement = complement,
                neighborhood = neighborhood,
                sectorId = sectorId
            )
        )
    }

    override suspend fun getPostalCodeAddress(postalCode: String) =
        networkApi.findPostalCodeAddressInfo(postalCode)?.toDomain()

    override suspend fun getAllSectors() = networkApi.getSectors().map { it.toDomain() }
}