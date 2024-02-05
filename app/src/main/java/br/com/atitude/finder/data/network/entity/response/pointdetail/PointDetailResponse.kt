package br.com.atitude.finder.data.network.entity.response.pointdetail

import br.com.atitude.finder.data.network.entity.response.address.AddressResponse
import br.com.atitude.finder.data.network.entity.response.address.PointDetailAddressResponse
import br.com.atitude.finder.data.network.entity.response.sector.SectorResponse
import br.com.atitude.finder.data.network.entity.response.sector.toDomain
import br.com.atitude.finder.domain.WeekDay
import br.com.atitude.finder.domain.pointdetail.PointDetail
import br.com.atitude.finder.extensions.toDate
import br.com.atitude.finder.extensions.toLocalDateTime
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.util.Date

data class PointDetailResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("leader") val leader: PointDetailLeaderResponse,
    @SerializedName("week_day") val weekDay: String,
    @SerializedName("hour") val hourDay: Int,
    @SerializedName("minute") val minuteDay: Int,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("tag") val tag: String,
    @SerializedName("sector") val sector: SectorResponse,
    @SerializedName("location") val address: PointDetailAddressResponse,
    @SerializedName("contact_phones") val contactPhones: List<PointDetailContactResponse>
)

fun PointDetailResponse.toDomain(): PointDetail =
    PointDetail(
        id = this.id,
        name = this.name,
        leaderName = this.leader.leaderName,
        weekDay = WeekDay.getByResponse(this.weekDay),
        hour = this.hourDay,
        minute = this.minuteDay,
        updatedAt = this.updatedAt.toLocalDateTime(),
        tag = this.tag,
        sector = this.sector.toDomain(),
        address = this.address.address,
        pointContacts = this.contactPhones.map { it.toDomain() },
        postalCode = this.address.postalCode
    )