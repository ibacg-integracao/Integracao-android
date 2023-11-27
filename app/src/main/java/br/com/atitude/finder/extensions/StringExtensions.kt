package br.com.atitude.finder.extensions

import br.com.atitude.finder.domain.PointTime

fun String.toPointTime(): PointTime {
    val tokens = this.split(":")
    val hour = tokens[0].toInt()
    val minutes = tokens[1].toInt()
    return PointTime(hour, minutes)
}