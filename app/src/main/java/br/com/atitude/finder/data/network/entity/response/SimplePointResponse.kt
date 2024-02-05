package br.com.atitude.finder.data.network.entity.response

import br.com.atitude.finder.data.network.entity.response.address.AddressResponse
import br.com.atitude.finder.data.network.entity.response.address.DistanceResponse
import br.com.atitude.finder.domain.SimplePoint
import br.com.atitude.finder.domain.WeekDay
import com.google.gson.annotations.SerializedName

data class SimplePointResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("location") val address: AddressResponse,
    @SerializedName("week_day") val weekDay: String,
    @SerializedName("hour") val hour: Int,
    @SerializedName("minute") val minute: Int,
    @SerializedName("tag") val tag: String,
    @SerializedName("distance") val distance: DistanceResponse?,
)

fun SimplePointResponse.toDomain() = SimplePoint(
    id = this.id,
    name = this.name,
    address = this.address.address,
    weekDay = WeekDay.getByResponse(this.weekDay),
    hour = this.hour,
    minute = this.minute,
    tag = this.tag,
    distance = this.distance?.distance
)