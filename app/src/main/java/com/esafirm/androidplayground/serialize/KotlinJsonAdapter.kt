package com.esafirm.androidplayground.serialize

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

@Suppress("UNCHECKED_CAST")
class KotlinJsonAdapter(
    private val json: Json,
    private val serialier: KSerializer<*>
) : JsonAdapter {

    override fun <T : Any> to(any: T): String {
        return json.encodeToString(serialier as KSerializer<T>, any)
    }

    override fun <T> from(jsonString: String): T {
        return json.decodeFromString(serialier as KSerializer<T>, jsonString)
    }
}

class KotlinJsonAdapterFactory(
    private val json: Json = Json { encodeDefaults = true }
) {
    fun of(serializer: KSerializer<*>) = KotlinJsonAdapter(json, serializer)
}
