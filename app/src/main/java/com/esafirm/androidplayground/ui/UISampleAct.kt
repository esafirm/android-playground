package com.esafirm.androidplayground.ui

import android.os.Bundle
import com.esafirm.androidplayground.common.BaseAct
import com.esafirm.androidplayground.common.ControllerMaker
import com.esafirm.androidplayground.common.MenuFactory
import com.esafirm.androidplayground.main.RouterAct
import com.esafirm.androidplayground.utils.ActivityStater
import java.util.*

class UISampleAct : BaseAct() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
                MenuFactory.create(this, Arrays.asList(
                        "Constraint Layout",
                        "Spinner Dropdown Position",
                        "Xfermode"
                )) { index -> navigateToIndex(index) }
        )
    }

    fun navigateToIndex(index: Int) {
        when (index) {
            0 -> ActivityStater.start(this, ConstraintLayoutAct::class.java)
            1 -> ActivityStater.start(this, SpinnerPositionAct::class.java)
            2 -> RouterAct.start(this, ControllerMaker { XfermodeController() })
        }
    }
}
