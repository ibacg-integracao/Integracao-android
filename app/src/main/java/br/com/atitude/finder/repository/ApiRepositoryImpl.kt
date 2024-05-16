package br.com.atitude.finder.repository

import br.com.atitude.finder.data.network.NetworkApi
import br.com.atitude.finder.data.network.entity.request.ChangePasswordRequest
import br.com.atitude.finder.data.network.entity.request.CreatePointRequest
import br.com.atitude.finder.data.network.entity.request.LoginRequest
import br.com.atitude.finder.data.network.entity.request.RegisterAccountRequest
import br.com.atitude.finder.data.network.entity.request.UpdatePointRequest
import br.com.atitude.finder.data.network.entity.response.pointdetail.toDomain
import br.com.atitude.finder.data.network.entity.response.search.toDomain
import br.com.atitude.finder.data.network.entity.response.sector.toDomain
import br.com.atitude.finder.data.network.entity.response.toDomain
import br.com.atitude.finder.data.network.entity.toDomain
import br.com.atitude.finder.data.network.entity.toUser
import br.com.atitude.finder.domain.PointContact
import br.com.atitude.finder.domain.PointState
import br.com.atitude.finder.domain.SearchParams
import br.com.atitude.finder.domain.SimplePoint
import br.com.atitude.finder.domain.UserManagerItem
import br.com.atitude.finder.domain.WeekDay
import br.com.atitude.finder.domain.pointdetail.PointDetail
import br.com.atitude.finder.domain.toRequest
import br.com.atitude.finder.extensions.toPointTime

class ApiRepositoryImpl(private val networkApi: NetworkApi) : ApiRepository {

    override suspend fun getAuthenticatedUser() = networkApi.getAuthenticatedUser().toUser()

    override suspend fun login(email: String, password: String) =
        networkApi.login(LoginRequest(email, password)).toDomain()

    override suspend fun getPointById(id: String): PointDetail =
        networkApi.getPointById(id).toDomain()

    override suspend fun deletePoint(id: String) {
        networkApi.deletePoint(id)
    }

    override suspend fun getAllPoints(): List<SimplePoint> =
        networkApi.getAllPoints().map { it.toDomain() }

    override suspend fun getWeekDays(): List<WeekDay> =
        networkApi.getWeekDays().map { WeekDay.getByResponse(it.name) }

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
        ).map { it.toDomain() }
    }

    override suspend fun searchPointsByAddressOrPostalCode(
        input: String,
        weekDays: List<String>,
        tags: List<String>,
        times: List<String>
    ): List<SimplePoint> {
        return networkApi.searchPointsByAddressOrPostalCode(
            input = input,
            weekDays = weekDays,
            tags = tags,
            times = times
        ).map { it.toDomain() }
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
    ) {
        networkApi.createPoint(
            CreatePointRequest(
                name = name,
                street = street,
                leaderName = leaderName,
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
                sectorId = sectorId,
                phoneContacts = phoneContacts.map { it.toRequest() },
                reference = reference
            )
        )
    }

    override suspend fun getPostalCodeAddress(postalCode: String) =
        networkApi.findPostalCodeAddressInfo(postalCode)?.toDomain()

    override suspend fun getAllSectors() = networkApi.getSectors().map { it.toDomain() }
    override suspend fun updatePoint(id: String, state: PointState?) =
        networkApi.updatePoint(id, UpdatePointRequest(state = state?.label)).toDomain()

    override suspend fun registerAccount(name: String, email: String, password: String) {
        networkApi.registerAccount(
            RegisterAccountRequest(
                name = name,
                email = email,
                password = password
            )
        )
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String) {
        networkApi.updatePassword(ChangePasswordRequest(oldPassword, newPassword))
    }

    override suspend fun getAllUsers(): List<UserManagerItem> =
        networkApi.getAllUsers().map { it.toDomain() }

    override suspend fun disableUser(userId: String) {
        networkApi.disableUser(userId)
    }

    override suspend fun enableUser(userId: String) {
        networkApi.enabledUser(userId)
    }

    override suspend fun deleteUser(userId: String) {
        networkApi.deleteNotAcceptedUser(userId)
    }
}