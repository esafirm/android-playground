package com.esafirm.androidplayground.rxjava2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.navigateTo
import com.esafirm.androidplayground.common.navigateToController
import com.esafirm.androidplayground.utils.ActivityStater
import com.esafirm.androidplayground.utils.menu

class RxJava2SampleController : BaseController() {

    private fun navigateToIndex(index: Int) {
        when (index) {
            0 -> ActivityStater.start(activity, RxJava2ObservableFlowableAct::class.java)
            1 -> ActivityStater.start(activity, RxJava2InteropAct::class.java)
            2 -> ActivityStater.start(activity, RxJava2NullItemAct::class.java)
            3 -> ActivityStater.start(activity, RxJava2RetryWhen::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return menu(
            "Observable vs Flowable" navigateTo { navigateToIndex(0) },
            "Interop with v1" navigateTo { navigateToIndex(1) },
            "Null item on Stream" navigateTo { navigateToIndex(2) },
            "RetryWhen" navigateTo { navigateToIndex(3) },
            "Single | Kotlin Infix Operator" navigateTo { navigateToIndex(4) },
            "Zip Multiple" navigateToController { RxJava2ZipController() }
        )
    }
}
