package com.esafirm.androidplayground.flipper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.Logger
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row
import retrofit2.Response


class FlipperController : BaseController() {

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
            logger()
        }
    }

    private fun putSharedPreference() {
        val eulaKey = "mykey"
        val pref = requiredContext.getSharedPreferences("playground", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(eulaKey, true).apply()
    }

    private fun triggerCrash() {

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