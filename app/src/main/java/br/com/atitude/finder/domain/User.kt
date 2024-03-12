package br.com.atitude.finder.domain

import com.google.gson.annotations.SerializedName
import java.util.Date

data class User(
    val id: String,
    val name: String,
    val email: String,
    val accepted: Boolean,
    val createdAt: Date,
    val updatedAt: Date
)