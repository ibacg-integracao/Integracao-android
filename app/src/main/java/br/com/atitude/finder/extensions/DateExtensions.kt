package br.com.atitude.finder.extensions

import java.util.Calendar
import java.util.Date

fun Date.toHour(): Pair<Int, Int> {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]
    return Pair(hour, minute)
}

fun Date.toDate(): Triple<Int, Int, Int> {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val day = calendar[Calendar.DAY_OF_MONTH]
    val month = calendar[Calendar.MONTH]
    val year = calendar[Calendar.YEAR]
    return Triple(day, month, year)
}