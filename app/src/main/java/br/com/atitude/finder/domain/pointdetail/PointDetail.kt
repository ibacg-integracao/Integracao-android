package br.com.atitude.finder.domain.pointdetail

import br.com.atitude.finder.domain.Sector
import br.com.atitude.finder.domain.WeekDay
import com.google.type.DateTime
import java.time.LocalDateTime
import java.util.Date

data class PointDetail(
    val id: String,
    val name: String,
    val leaderName: String,
    val weekDay: WeekDay,
    val hour: Int,
    val minute: Int,
    val updatedAt: Date,
    val tag: String,
    val sector: Sector,
    val address: String,
    val postalCode: String,
    val pointContacts: List<PointDetailContact>
)