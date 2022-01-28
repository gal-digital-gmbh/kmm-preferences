package de.galdigital.preferences

expect class SharedPreferences {
    fun getBoolean(key: String, default: Boolean = false): Boolean
    fun setBoolean(key: String, value: Boolean)
    fun getString(key: String, default: String? = null): String?
    fun setString(key: String, value: String?)
}
