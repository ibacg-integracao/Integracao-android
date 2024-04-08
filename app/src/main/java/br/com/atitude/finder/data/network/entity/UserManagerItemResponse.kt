package br.com.atitude.finder.data.network.entity

import br.com.atitude.finder.domain.UserManagerItem
import com.google.gson.annotations.SerializedName
import java.util.Date

data class UserManagerItemResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("accepted") val accepted: Boolean,
    @SerializedName("created_at") val createdAt: Date
)

fun UserManagerItemResponse.toDomain() = UserManagerItem(
    id = this.id,
    name = this.name,
    accepted = this.accepted,
    createdAt = this.createdAt
)