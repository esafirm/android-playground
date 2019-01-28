package com.esafirm.androidplayground.lifecycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.fragment.app.Fragment
import com.esafirm.androidplayground.utils.logMeasure
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class FragmentLifecycle : Fragment() {

    companion object {
        const val MAIN_ID = 0x2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val asyncLayoutInflater = AsyncLayoutInflater(requireContext())

        return FrameLayout(requireContext()).apply {
            id = MAIN_ID
            layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    override fun onStart() {
        super.onStart()
        runBlocking {
            delay(500)
        }
    }

    override fun onResume() {
        runBlocking {
            delay(2000)
        }
        super.onResume()
    }
}

class ActivityLifecycle : AppCompatActivity() {

    companion object {
        const val MAIN_ID = 0x1
    }

    override fun onCreate(savedInstanceState: Bundle?) = logMeasure("onCreate") {
        super.onCreate(savedInstanceState)
        setContentView(FrameLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            )
            id = MAIN_ID
        })

        supportFragmentManager.beginTransaction()
                .add(MAIN_ID, FragmentLifecycle())
                .commitNow()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) = logMeasure("onPostCreate"){
        super.onPostCreate(savedInstanceState)
    }

    override fun onStart() = logMeasure("onStart") {
        super.onStart()
    }

    override fun onResume() = logMeasure("onResume") {
        super.onResume()
    }

    override fun onPostResume() = logMeasure("onPostResume") {
        super.onPostResume()
    }
}
