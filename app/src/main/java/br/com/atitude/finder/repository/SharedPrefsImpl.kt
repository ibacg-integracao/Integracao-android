package br.com.atitude.finder.repository

import android.content.SharedPreferences

private const val EMAIL = "email"
private const val PASSWORD = "password"
private const val TOKEN = "token"

class SharedPrefsImpl(private val sharedPreferences: SharedPreferences) : SharedPrefs {
    override fun getToken(): String = sharedPreferences.getString(TOKEN, "").orEmpty()
    override fun setToken(token: String) {
        sharedPreferences.edit().putString(TOKEN, token).apply()
    }

    override fun getEmail() = sharedPreferences.getString(EMAIL, "").orEmpty()

    override fun getPassword() = sharedPreferences.getString(PASSWORD, "").orEmpty()

    override fun setEmail(email: String) {
        sharedPreferences.edit().putString(EMAIL, email).apply()
    }

    override fun setPassword(password: String) {
        sharedPreferences.edit().putString(PASSWORD, password).apply()
    }

    override fun hasToken() = sharedPreferences.contains(TOKEN)

    override fun clearToken() {
        sharedPreferences.edit().remove(TOKEN).apply()
    }

}