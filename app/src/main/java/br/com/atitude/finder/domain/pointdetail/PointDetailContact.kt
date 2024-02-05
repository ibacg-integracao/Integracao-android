package br.com.atitude.finder.domain.pointdetail

import br.com.atitude.finder.domain.Gender

data class PointDetailContact(
    val id: String,
    val name: String,
    val contactPhone: String,
    val gender: Gender
) {
    fun getPlainPhone() = contactPhone.replace("(", "").replace(")", "").replace(" ", "")
}