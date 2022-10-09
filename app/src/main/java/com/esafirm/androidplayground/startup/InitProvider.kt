package com.esafirm.androidplayground.startup

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.pm.ProviderInfo
import android.database.Cursor
import android.net.Uri
import com.esafirm.androidplayground.libs.Logger
import com.esafirm.androidplayground.utils.ProcessUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class InitProvider : ContentProvider() {

    abstract fun onInit(context: Context)

    override fun attachInfo(context: Context?, info: ProviderInfo?) {
        super.attachInfo(context, info)

        if (context == null) {
            throw IllegalStateException("Context should not be null")
        } else {
            Logger.log("Name: ${ProcessUtils.isMainProcess(context)}")
            GlobalScope.launch {
                onInit(context)
            }
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return null
    }

    override fun onCreate() = true

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? = null
}
