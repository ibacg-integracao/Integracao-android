package br.com.atitude.finder.domain

import java.util.Date

data class UserManagerItem(
    val id: String,
    val name: String,
    val accepted: Boolean,
    val createdAt: Date
)