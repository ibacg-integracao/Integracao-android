package br.com.atitude.finder.data.network.entity.response.pointdetail

import com.google.gson.annotations.SerializedName

data class PointDetailLeaderResponse(
    @SerializedName("leader_name") val leaderName: String
)