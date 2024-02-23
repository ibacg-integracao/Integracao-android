package br.com.atitude.finder.domain

data class SimplePoint(
    val id: String,
    val name: String,
    val address: String,
    val reference: String?,
    val weekDay: WeekDay,
    val hour: Int,
    val minute: Int,
    val tag: String,
    val distance: Double?,
) {
    fun getPreciseDistance(): DistancePrecision? {
        if (distance == null) return null

        return DistancePrecision(
            if (distance >= 1000) DistanceUnit.KILOMETERS else DistanceUnit.METERS,
            if (distance >= 1000) distance / 1000 else distance,
        )
    }

    enum class DistanceUnit(val symbol: String) {
        METERS("m"), KILOMETERS("km");
    }

    inner class DistancePrecision(val unit: DistanceUnit, val distance: Double)
}