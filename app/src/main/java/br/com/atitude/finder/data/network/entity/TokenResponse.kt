package br.com.atitude.finder.data.network.entity

import br.com.atitude.finder.domain.Token
import com.google.gson.annotations.SerializedName
import java.util.Date

data class TokenResponse(
    @SerializedName("token") val token: String,
    @SerializedName("expires_at") val expiresAt: Date
)

fun TokenResponse.toDomain() = Token(
    token = this.token,
    expiresAt = expiresAt
)