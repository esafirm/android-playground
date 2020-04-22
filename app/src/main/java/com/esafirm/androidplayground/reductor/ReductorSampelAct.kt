package com.esafirm.androidplayground.reductor

import android.os.Bundle
import com.esafirm.androidplayground.common.BaseAct
import com.esafirm.androidplayground.common.MenuFactory
import com.esafirm.androidplayground.utils.ActivityStater
import java.util.*

class ReductorSampelAct : BaseAct() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
                MenuFactory.create(this, listOf(
                        "Counter"
                )) { navigateToIndex() }
        )
    }

    private fun navigateToIndex() {
        ActivityStater.start(this, ReductorCounterAct::class.java)
    }
}