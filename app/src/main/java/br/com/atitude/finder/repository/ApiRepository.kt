package br.com.atitude.finder.repository

import br.com.atitude.finder.data.network.entity.TokenResponse
import br.com.atitude.finder.domain.PointContact
import br.com.atitude.finder.domain.PointState
import br.com.atitude.finder.domain.PointTime
import br.com.atitude.finder.domain.PostalCodeAddressInfo
import br.com.atitude.finder.domain.SearchParams
import br.com.atitude.finder.domain.Token
import br.com.atitude.finder.domain.Sector
import br.com.atitude.finder.domain.SimplePoint
import br.com.atitude.finder.domain.User
import br.com.atitude.finder.domain.UserManagerItem
import br.com.atitude.finder.domain.WeekDay
import br.com.atitude.finder.domain.pointdetail.PointDetail

interface ApiRepository {

    suspend fun getAuthenticatedUser(): User

    suspend fun login(email: String, password: String): Token

    suspend fun getPointById(id: String): PointDetail

    suspend fun deletePoint(id: String)

    suspend fun getWeekDays(): List<WeekDay>

    suspend fun searchPointsByAddressOrPostalCode(
        input: String,
        pointName: String?,
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
        postalCode: String,
        number: Int?,
        leaderName: String,
        tag: String,
        hour: Int,
        minutes: Int,
        weekDay: String,
        sectorId: String,
        phoneContacts: List<PointContact>,
        reference: String?
    )

    suspend fun getPostalCodeAddress(postalCode: String): PostalCodeAddressInfo?

    suspend fun getAllSectors(): List<Sector>

    suspend fun updatePoint(id: String, state: PointState?): SimplePoint

    suspend fun registerAccount(name: String, email: String, password: String)

    suspend fun changePassword(oldPassword: String, newPassword: String)

    suspend fun getAllUsers(): List<UserManagerItem>

    suspend fun disableUser(userId: String)
    suspend fun enableUser(userId: String)
    suspend fun deleteUser(userId: String)
}