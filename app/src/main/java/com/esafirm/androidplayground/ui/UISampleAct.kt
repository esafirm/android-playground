package com.esafirm.androidplayground.ui

import android.os.Bundle
import com.esafirm.androidplayground.common.BaseAct
import com.esafirm.androidplayground.common.MenuFactory
import com.esafirm.androidplayground.utils.ActivityStater
import java.util.*

class UISampleAct : BaseAct() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
                MenuFactory.create(this, Arrays.asList(
                        "Constraint Layout"
                )) { index -> navigateToIndex(index) }
        )
    }

    fun navigateToIndex(index: Int) {
        when (index) {
            0 -> ActivityStater.start(this, ConstraintLayoutAct::class.java)
        }
    }
}
