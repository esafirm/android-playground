package com.esafirm.androidplayground.flipper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.*
import retrofit2.Response


class FlipperController : BaseController() {

    private val databaseHelper by lazy { DatabaseHelper(requiredContext) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            button("Trigger crash") {
                triggerCrash()
            }
            button("Call API") {
                callApi()
            }
            button("Shared Preference put") {
                putSharedPreference()
            }
            button("Add data to Database") {
                insertDatabase()
            }
            button("Get all data from Database") {
                getAllData()
            }
            logger()
        }
    }

    private fun insertDatabase() {
        val data = FlipperData().apply {
            name = "Robot_${System.currentTimeMillis()}"
        }
        databaseHelper.insertData(data)

        Logger.log("Inserting ${data.name}")
    }

    private fun getAllData() {
        val alls = databaseHelper.getAll().fold("") { acc, flipperData ->
            "$acc\n${flipperData.name}"
        }
        Logger.log(alls)
    }

    private fun putSharedPreference() {
        val eulaKey = "mykey"
        val pref = requiredContext.getSharedPreferences("playground", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(eulaKey, true).apply()
    }

    private fun triggerCrash() {
        CrashHandler.logCrash(IllegalArgumentException("Hi from Flipper controller!"))
    }

    private fun callApi() {
        ApiCaller.getEmployee().enqueue(object : retrofit2.Callback<List<Employee>> {
            override fun onFailure(call: retrofit2.Call<List<Employee>>, t: Throwable) {
                Logger.log("onFailure")
            }

            override fun onResponse(call: retrofit2.Call<List<Employee>>, response: Response<List<Employee>>) {
                Logger.log("onResponse")
            }
        })
    }
}