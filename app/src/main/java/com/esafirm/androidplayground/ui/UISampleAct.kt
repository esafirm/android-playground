package com.esafirm.androidplayground.ui

import android.os.Bundle
import com.esafirm.androidplayground.common.BaseAct
import com.esafirm.androidplayground.common.MenuFactory
import com.esafirm.androidplayground.common.navigateTo
import com.esafirm.androidplayground.common.navigateToController
import com.esafirm.androidplayground.ui.attach.AttachTestController
import com.esafirm.androidplayground.ui.cliptopadding.ClipToPaddingController
import com.esafirm.androidplayground.ui.grayscalefilter.GrayscaleTestController
import com.esafirm.androidplayground.ui.nestedrecyclerview.NestedRecyclerViewController
import com.esafirm.androidplayground.ui.nestedscroll.NestedScrollingController
import com.esafirm.androidplayground.ui.vectortest.VectorTestController
import com.esafirm.androidplayground.ui.zipperview.ZipperViewController
import com.esafirm.androidplayground.utils.ActivityStater

class UISampleAct : BaseAct() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            MenuFactory.create(this, listOf(
                "Attach Listener" navigateToController { AttachTestController() },
                "Constraint Layout" navigateTo { ActivityStater.start(this, ConstraintLayoutAct::class.java) },
                "Spinner Dropdown Position" navigateTo { ActivityStater.start(this, SpinnerPositionAct::class.java) },
                "Xfermode" navigateToController { XfermodeController() },
                "View Drag Helper" navigateToController { ViewDragHelperController() },
                "Clip to Padding" navigateToController { ClipToPaddingController() },
                "Nested Scroll Test - RecyclerView inside Coordinator" navigateToController { NestedScrollingController() },
                "Zipper View" navigateToController { ZipperViewController() },
                "Nested RecyclerView Adapter" navigateToController { NestedRecyclerViewController() },
                "Grayscale Screen Test" navigateToController { GrayscaleTestController() },
                "VectorDrawable Test" navigateToController { VectorTestController() }
            ))
        )
    }
}
