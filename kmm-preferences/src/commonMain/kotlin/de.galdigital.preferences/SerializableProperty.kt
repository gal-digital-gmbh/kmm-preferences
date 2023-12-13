package de.galdigital.preferences

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

@InternalSerializationApi
class SerializableProperty<T : Any>(
    private val sharedPreferences: SharedPreferences,
    private val jsonSerializer: Json,
    private val name: String,
    private val default: T?,
    private val type: KClass<T>,
    private val listener: (T?) -> Unit
) : ReadWriteProperty<Any?, T?> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return try {
            jsonSerializer.decodeFromString(
                deserializer = type.serializer(),
                string = sharedPreferences.getString(name).orEmpty()
            )
        } catch (_: Throwable) {
            default
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        try {
            if (value == null) {
                sharedPreferences.setString(name, null)
                return listener.invoke(value)
            }
            val serialized = jsonSerializer.encodeToString(
                serializer = type.serializer(),
                value = value
            )
            sharedPreferences.setString(name, serialized)
            listener.invoke(value)
        } catch (_: Throwable) {
        }
    }

}

@InternalSerializationApi
inline fun <reified T : Any> serializable(
    storage: SharedPreferences,
    jsonSerializer: Json,
    name: String,
    default: T?,
    noinline listener: ((T?) -> Unit) = {}
): SerializableProperty<T> = SerializableProperty(storage, jsonSerializer, name, default, T::class, listener)
