package de.galdigital.preferences

import android.content.Context
import android.content.SharedPreferences

actual class SharedPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(null, Context.MODE_PRIVATE)
    }

    actual fun getBoolean(key: String, default: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, default)
    }

    actual fun setBoolean(key: String, value: Boolean) {
        sharedPreferences.edit()
            .putBoolean(key, value)
            .apply()
    }

    actual fun getString(key: String, default: String?): String? {
        return sharedPreferences.getString(key, default)
    }

    actual fun setString(key: String, value: String?) = when (value) {
        null -> sharedPreferences.edit()
            .remove(key)
            .apply()
        else -> sharedPreferences.edit().putString(key, value).apply()
    }
}
