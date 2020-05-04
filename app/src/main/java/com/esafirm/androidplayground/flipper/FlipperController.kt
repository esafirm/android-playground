package com.esafirm.androidplayground.flipper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.CrashHandler
import com.esafirm.androidplayground.utils.Logger
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row
import retrofit2.Call
import retrofit2.Response


class FlipperController : BaseController() {

    private val databaseHelper by lazy { DatabaseHelper(requiredContext) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            button("Trigger crash") {
                triggerCrash()
            }
            button("Call API [GET]") {
                callApi()
            }
            button("Call API [POST]") {
                callPostApi()
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
        val all = databaseHelper.getAll().fold("") { acc, flipperData ->
            "$acc\n${flipperData.name}"
        }
        Logger.log(all)
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
                Logger.log("onFailure: $t")
            }

            override fun onResponse(call: retrofit2.Call<List<Employee>>, response: Response<List<Employee>>) {
                Logger.log("onResponse")
            }
        })
    }

    private fun callPostApi() {
        val employee = Employee(
            employee_age = "22",
            employee_name = "Someone",
            employee_salary = "Cukup",
            id = "123123",
            profile_image = "https://www.google.com"
        )
        ApiCaller.createEmployee(employee).enqueue(object : retrofit2.Callback<Employee> {
            override fun onFailure(call: Call<Employee>, t: Throwable) {
                Logger.log("POST onFailure: $t")
            }

            override fun onResponse(call: Call<Employee>, response: Response<Employee>) {
                Logger.log("POST onResponse")
            }
        })
    }
}