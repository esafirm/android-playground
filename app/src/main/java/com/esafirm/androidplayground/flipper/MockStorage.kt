package com.esafirm.androidplayground.flipper

import android.content.SharedPreferences
import com.facebook.flipper.plugins.network.NetworkReporter
import com.google.gson.Gson
import java.util.*

interface MockStorage {
    fun put(info: PartialRequestInfo, response: NetworkReporter.ResponseInfo)
    fun get(info: PartialRequestInfo): NetworkReporter.ResponseInfo?
    fun contains(info: PartialRequestInfo): Boolean
    fun clear()
}

interface MockAdapter<TARGET> {
    fun serializeInfo(info: PartialRequestInfo): TARGET
    fun serializerResponse(response: NetworkReporter.ResponseInfo): TARGET

    fun deserializeInfo(target: TARGET): PartialRequestInfo
    fun deserializeResponse(target: TARGET): NetworkReporter.ResponseInfo
}

class MemoryMockStorage : MockStorage {

    private val map: MutableMap<PartialRequestInfo, NetworkReporter.ResponseInfo> = HashMap(0)

    override fun put(info: PartialRequestInfo, response: NetworkReporter.ResponseInfo) {
        if (!map.containsKey(info)) {
            map[info] = response
        }
    }

    override fun get(info: PartialRequestInfo) = map[info]

    override fun contains(info: PartialRequestInfo) = map.containsKey(info)

    override fun clear() {
        map.clear()
    }
}

class MockJsonAdapter(private val gson: Gson) : MockAdapter<String> {

    override fun serializeInfo(info: PartialRequestInfo): String {
        return gson.toJson(info)
    }

    override fun serializerResponse(response: NetworkReporter.ResponseInfo): String {
        return gson.toJson(response)
    }

    override fun deserializeInfo(target: String): PartialRequestInfo {
        return gson.fromJson(target, PartialRequestInfo::class.java)
    }

    override fun deserializeResponse(target: String): NetworkReporter.ResponseInfo {
        return gson.fromJson(target, NetworkReporter.ResponseInfo::class.java)
    }
}

class PreferencesBackedMockStorage(
    private val pref: SharedPreferences,
    private val adapter: MockAdapter<String>,
    private val memoryMockStorage: MockStorage = MemoryMockStorage()
) : MockStorage {

    override fun put(info: PartialRequestInfo, response: NetworkReporter.ResponseInfo) {
        memoryMockStorage.put(info, response)

        pref.edit()
            .putString(adapter.serializeInfo(info), adapter.serializerResponse(response))
            .apply()
    }

    override fun get(info: PartialRequestInfo): NetworkReporter.ResponseInfo? {
        val responseInfo = memoryMockStorage.get(info)
        return responseInfo ?: getPref(info)
    }

    private fun getPref(info: PartialRequestInfo): NetworkReporter.ResponseInfo? {
        val key = adapter.serializeInfo(info)
        val result = pref.getString(key, null)
        return result?.let { adapter.deserializeResponse(result) }
    }

    override fun contains(info: PartialRequestInfo): Boolean {
        return when (val memoryResult = memoryMockStorage.contains(info)) {
            true -> memoryResult
            false -> pref.contains(adapter.serializeInfo(info))
        }
    }

    override fun clear() {
        memoryMockStorage.clear()
        pref.edit().clear().apply()
    }
}