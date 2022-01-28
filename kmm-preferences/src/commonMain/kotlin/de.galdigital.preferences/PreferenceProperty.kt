package de.galdigital.preferences

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class PreferenceProperty<T : Any>(
    private val preferences: SharedPreferences,
    private val name: String,
    private val default: T?,
    private val propertyClass: KClass<T>,
    private val listener: ((T?) -> Unit)
) : ReadWriteProperty<Any?, T?> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        val res: Any = when (propertyClass) {
            String::class -> preferences.getString(name, default as? String ?: "") as Any
            Boolean::class -> preferences.getBoolean(name, default as? Boolean ?: false)
            else -> throw IllegalArgumentException("This type cannot be saved into Preferences")
        }
        @Suppress("UNCHECKED_CAST")
        return res as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        when (propertyClass) {
            String::class -> preferences.setString(name, value as? String)
            Boolean::class -> preferences.setBoolean(name, value as Boolean)
            else -> throw IllegalArgumentException("This type cannot be saved into Preferences")
        }
        listener.invoke(value)
    }
}

inline fun <reified T : Any> preference(
    preferences: SharedPreferences,
    name: String,
    default: T?,
    noinline listener: ((T?) -> Unit) = {}
): PreferenceProperty<T> {
    return PreferenceProperty(preferences, name, default, T::class, listener)
}
