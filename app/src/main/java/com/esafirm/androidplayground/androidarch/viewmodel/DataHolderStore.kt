package com.esafirm.androidplayground.androidarch.viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.esafirm.androidplayground.PlaygroundApp
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataHolderStore: DataStore<Preferences> by preferencesDataStore(name = "data_holder")

class DataHolderStore(private val context: Context = PlaygroundApp.appContext()) {

    private val store get() = context.dataHolderStore

    suspend fun setData(key: String, data: String) {
        val prefKey = stringPreferencesKey(key)
        store.edit { storage ->
            storage[prefKey] = data
        }
    }

    suspend fun getData(key: String): String {
        val prefKey = stringPreferencesKey(key)
        val result = store.data.catch {
            it.printStackTrace()
            emptyPreferences()
        }.map { pref ->
            pref[prefKey] ?: ""
        }.first()

        // Delete before return
        store.edit { pref -> pref.remove(prefKey) }

        return result
    }
}