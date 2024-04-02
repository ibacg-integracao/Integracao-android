package br.com.atitude.finder.repository

interface SharedPrefs {
    fun getToken(): String
    fun setToken(token: String)
    fun getEmail(): String
    fun getPassword(): String
    fun setEmail(email: String)
    fun setPassword(password: String)
    fun hasToken(): Boolean
    fun clearToken()
}