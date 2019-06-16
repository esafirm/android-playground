package com.esafirm.androidplayground.startup

import android.content.Context

class LongLibInitProvider : InitProvider() {
    override fun onInit(context: Context) {
        LongInitLibrary()
    }
}