package com.esafirm.androidplayground.rxjava2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.esafirm.androidplayground.common.BaseAct
import com.esafirm.androidplayground.common.MenuFactory
import com.esafirm.androidplayground.utils.ActivityStater

class RxJava2SampleAct : BaseAct() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            MenuFactory.create(this, listOf(
                "Observable vs Flowable",
                "Interop with v1",
                "Null item on Stream",
                "RetryWhen",
                "Single | Kotlin Infix Operator"
            )) {
                this.navigateToIndex(it)
            }
        )
    }

    private fun navigateToIndex(index: Int) {
        when (index) {
            0 -> ActivityStater.start(this, RxJava2ObservableFlowableAct::class.java)
            1 -> ActivityStater.start(this, RxJava2InteropAct::class.java)
            2 -> ActivityStater.start(this, RxJava2NullItemAct::class.java)
            3 -> ActivityStater.start(this, RxJava2RetryWhen::class.java)
        }
    }

    companion object {

        /* --------------------------------------------------- */
        /* > Stater */
        /* --------------------------------------------------- */

        fun start(context: Context) {
            context.startActivity(
                Intent(context, RxJava2SampleAct::class.java))
        }
    }
}
