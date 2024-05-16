package br.com.atitude.finder.data.network.entity.response.sector

import br.com.atitude.finder.domain.Sector
import com.google.gson.annotations.SerializedName

data class SectorResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("leader_name") val leaderName: String
)

fun SectorResponse.toDomain(): Sector = Sector(
    id = this.id,
    name = this.name,
    leaderName = this.leaderName
)