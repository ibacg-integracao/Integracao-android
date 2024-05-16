package br.com.atitude.finder.extensions

import br.com.atitude.finder.domain.PointTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.toPointTime(): PointTime {
    val tokens = this.split(":")
    val hour = tokens[0].toInt()
    val minutes = tokens[1].toInt()
    return PointTime(hour, minutes)
}

fun String.toLocalDateTime(): Date {
    val formatted = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    return formatted.parse(this) ?: throw RuntimeException("couldn't parse date $this")
}