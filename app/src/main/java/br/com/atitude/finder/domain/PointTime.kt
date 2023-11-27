package br.com.atitude.finder.domain

data class PointTime(
    val hour: Int,
    val minutes: Int
) {
    override fun toString(): String {
        return "$hour:$minutes"
    }
}