package br.com.atitude.finder.domain

import br.com.atitude.finder.data.network.entity.request.CreatePointContactRequest
import br.com.atitude.finder.presentation._base.toPhoneFormat

data class PointContact(
    val name: String,
    val contact: String,
    val gender: Gender
) {
    fun toSimplePhone() = this.copy(contact = this.contact.toPhoneFormat(true))
}

fun PointContact.toRequest(): CreatePointContactRequest =
    CreatePointContactRequest(
        name = this.name,
        gender = this.gender.letter,
        phone = this.contact
    )