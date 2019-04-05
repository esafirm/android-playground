package com.esafirm.androidplayground.coroutines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.esafirm.androidplayground.utils.Logger
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BehaviorSubjectController : Controller() {

    private val subject = BehaviorSubjectCoroutine<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
            Logger.getLogView(container.context)

    override fun onAttach(view: View) {
        super.onAttach(view)

        subject.post("1")
        subject.post("2")

        subject.subscribe(throttle = 1000) {
            Logger.log("From subject: $it")
        }

        subject.post("3")
        subject.post("4")

        GlobalScope.launch {
            delay(1_000)
            subject.post("6")
            delay(1_000)
            subject.post("7")
            subject.post("8")
        }

    }
}