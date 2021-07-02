package com.esafirm.androidplayground.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.esafirm.androidplayground.R
import com.esafirm.androidplayground.utils.ActivityStater

class ThemedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(R.color.white)
        setContentView(TextView(this).apply {
            text = "Themed"
            textSize = 28F

            setOnClickListener {
                ActivityStater.start(this@ThemedActivity, ThemedActivity::class.java)
            }
        })
    }
}