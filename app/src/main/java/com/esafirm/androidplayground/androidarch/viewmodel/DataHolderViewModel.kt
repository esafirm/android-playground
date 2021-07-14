package com.esafirm.androidplayground.androidarch.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DataHolderViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    companion object {
        private const val KEY_IS_RESTORED = "Key.IsRestored"
        private const val KEY_LIST_OF_KEYS = "Key.ListOfKeys"
    }

    private val store = DataHolderStore()

    val isRestoreState by lazy {
        if (savedStateHandle.get<Boolean>(KEY_IS_RESTORED) == null) {
            savedStateHandle[KEY_IS_RESTORED] = true
            return@lazy false
        }
        return@lazy true
    }

    val dataStream = MutableLiveData<String>()

    init {
        if (isRestoreState) {
            restoreData()
        }
    }

    private fun restoreData() {
        // restore data
        launch {
            val key = getDataKey()
            if (key.isNotEmpty()) {
                val value = store.getData(key)
                if (value.isNotEmpty()) {
                    dataStream.postValue(value)
                }
            }
        }
    }

    private fun getDataKey(): String = savedStateHandle[KEY_LIST_OF_KEYS] ?: ""

    private fun setKey(key: String) {
        savedStateHandle[KEY_LIST_OF_KEYS] = key
    }

    fun setData(key: String, data: String) {
        dataStream.postValue(data)
        setKey(key)

        launch {
            store.setData(key, data)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("DataHolder", "onCleared")
    }
}