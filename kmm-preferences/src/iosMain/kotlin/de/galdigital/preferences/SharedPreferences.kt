package de.galdigital.preferences

import platform.Foundation.NSUserDefaults

actual class SharedPreferences() {

    private val sharedPreferences: NSUserDefaults by lazy {
        NSUserDefaults.standardUserDefaults
    }

    private fun foundKey(key: String): Boolean = sharedPreferences.objectForKey(key) != null

    actual fun getBoolean(key: String, default: Boolean): Boolean {
        if (!foundKey(key)) {
            return default
        }

        return sharedPreferences.boolForKey(key)
    }

    actual fun setBoolean(key: String, value: Boolean) {
        sharedPreferences.setBool(value, key)
    }

    actual fun getString(key: String, default: String?): String? {
        if (!foundKey(key)) {
            return default
        }

        return sharedPreferences.stringForKey(key)
    }

    actual fun setString(key: String, value: String?) {
        if (value == null) {
            sharedPreferences.removeObjectForKey(key)
        } else {
            sharedPreferences.setObject(value, key)
        }
    }
}
