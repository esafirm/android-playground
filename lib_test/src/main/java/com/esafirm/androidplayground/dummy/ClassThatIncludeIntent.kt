package com.esafirm.androidplayground.dummy

import android.content.Intent

class ClassThatIncludeIntent {

    companion object {
        const val DEFAULT_VALUE = "empty_string"
    }

    fun getIntentValueForKey(intent: Intent, key: String): String {
        return intent.getStringExtra(key) ?: DEFAULT_VALUE
    }
}