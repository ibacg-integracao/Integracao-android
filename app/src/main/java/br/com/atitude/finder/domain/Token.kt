package br.com.atitude.finder.domain

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Token(
    val token: String,
    val expiresAt: Date
)