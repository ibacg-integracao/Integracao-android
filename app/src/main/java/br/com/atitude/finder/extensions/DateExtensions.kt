package br.com.atitude.finder.extensions

import android.content.Context
import br.com.atitude.finder.R
import java.util.Calendar
import java.util.Date

fun Date.toDMYString(context: Context): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val day = calendar[Calendar.DAY_OF_MONTH]
    val month = calendar[Calendar.MONDAY]
    val year = calendar[Calendar.YEAR]
    return context.getString(R.string.dmy_date_format, day, month, year)
}

fun Date.toHMString(context: Context): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]
    return context.getString(R.string.hm_date_format, hour, minute)
}