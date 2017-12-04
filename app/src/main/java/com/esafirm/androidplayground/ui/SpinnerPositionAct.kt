package com.esafirm.androidplayground.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.esafirm.androidplayground.R
import com.esafirm.androidplayground.common.BaseAct

class SpinnerPositionAct : BaseAct() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui_spinner)

        val context = this
        findViewById<Spinner>(R.id.spinner_data)
                .apply {
                    dropDownVerticalOffset = 500
                    adapter = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, arrayListOf("Satu", "Dua", "Tiga"))
                }
    }
}
