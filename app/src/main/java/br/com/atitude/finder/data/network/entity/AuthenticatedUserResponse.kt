package br.com.atitude.finder.data.network.entity

import br.com.atitude.finder.domain.User
import com.google.gson.annotations.SerializedName
import java.util.Date

data class AuthenticatedUserResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("accepted") val accepted: Boolean,
    @SerializedName("created_at") val createdAt: Date,
    @SerializedName("updated_at") val updatedAt: Date
)

fun AuthenticatedUserResponse.toUser(): User = User(
    id = this.id,
    name = this.name,
    email = this.email,
    accepted = this.accepted,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)