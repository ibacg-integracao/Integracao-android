package br.com.atitude.finder.presentation._base

import android.telephony.PhoneNumberUtils

fun String.toPhoneFormat(simple: Boolean = false): String {
    var phone = PhoneNumberUtils.formatNumber(this, "BR")

    if (simple) {
        phone = phone.replace(" ", "")
        phone = phone.replace("-", "")
    }

    return phone
}