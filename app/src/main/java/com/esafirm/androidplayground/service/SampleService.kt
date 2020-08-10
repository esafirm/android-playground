package com.esafirm.androidplayground.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.esafirm.androidplayground.utils.Logger

class SampleService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStart(intent: Intent?, startId: Int) {
        Log.d("Service", "heya")
        super.onStart(intent, startId)
    }
}