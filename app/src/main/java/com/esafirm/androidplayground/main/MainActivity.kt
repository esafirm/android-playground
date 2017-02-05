package com.esafirm.androidplayground.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.esafirm.androidplayground.common.MenuFactory
import com.esafirm.androidplayground.dagger.example.DaggerSampleAct
import com.esafirm.androidplayground.rxjava2.RxJava2SampleAct
import com.esafirm.androidplayground.ui.UISampleAct
import com.esafirm.androidplayground.utils.ActivityStater
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
                MenuFactory.create(this,
                        Arrays.asList(
                                "Dagger Example",
                                "RxJava2 Example",
                                "UI Example"
                        )
                ) { index -> navigateToPage(index) }
        )
    }

    private fun navigateToPage(index: Int) {
        when (index) {
            0 -> DaggerSampleAct.start(this)
            1 -> RxJava2SampleAct.start(this)
            else -> ActivityStater.start(this, UISampleAct::class.java)
        }
    }
}
