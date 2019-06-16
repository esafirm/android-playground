package com.esafirm.androidplayground.startup

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class LongInitLibrary {

    companion object {
        private const val TAG = "Library"
    }

    init {
        Log.d(TAG, "Before init")
        STracker.start()
        runBlocking {
            delay(2000)
        }
    }
}