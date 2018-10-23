package com.esafirm.androidplayground.others

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ListView
import com.esafirm.androidplayground.R
import com.esafirm.androidplayground.utils.Logger

class Test(val view: View?)

class LeakTestActivity : AppCompatActivity() {

    companion object {
        var leakyView: View? = null
    }

    val handler = Handler()
    var test: Test? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<ListView>(R.id.listview)
        test = Test(view)
        leakyView = view

        val v = findViewById<View>(R.id.activity_main)
        v.setOnClickListener {
            handler.postDelayed({
                Logger.log("isFinishing: $isFinishing")
                Logger.log(test!!.view.toString())
            }, 3000)
            finish()
        }
    }
}
