package com.esafirm.androidplayground.conductor.viewstate

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import java.util.concurrent.atomic.AtomicBoolean

class ViewStateSampleController : Controller() {

    private val isSaveViewState = AtomicBoolean(false)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val matchParent = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return ScrollView(container.context).apply {
            layoutParams = matchParent
        }.also {
            val text = TextView(container.context).apply {
                layoutParams = matchParent
                textSize = 20f
                (0..100).forEach { i ->
                    append("$i\n")
                }
            }

            it.addView(LinearLayout(container.context).apply {
                layoutParams = matchParent
                orientation = LinearLayout.VERTICAL
                addView(text)
                addView(createRetainCheckBox(container.context, matchParent))
                addView(createSaveViewStateCheckBox(container.context, matchParent))
                addView(Button(container.context).apply {
                    layoutParams = matchParent
                    setText("Push Another")
                    setOnClickListener { pushAnother() }
                })
            })
        }
    }

    override fun onSaveViewState(view: View, outState: Bundle) {
        super.onSaveViewState(view, outState)
        if (isSaveViewState.get()) {
            outState.putInt("SCROLL_Y", view.let { it as ScrollView }.scrollY)
        }
    }

    override fun onRestoreViewState(view: View, savedViewState: Bundle) {
        if (isSaveViewState.get()) {
            val scrollView = view as ScrollView
            scrollView.post {
                scrollView.scrollTo(0, savedViewState.getInt("SCROLL_Y", 0))
            }
        }
    }


    private fun createSaveViewStateCheckBox(context: Context, params: ViewGroup.LayoutParams): CheckBox =
            CheckBox(context).apply {
                layoutParams = params
                text = "Save View State"
                isChecked = isSaveViewState.get()
                setOnCheckedChangeListener { _, isChecked ->
                    isSaveViewState.set(isChecked)
                }
            }

    private fun createRetainCheckBox(context: Context, params: ViewGroup.LayoutParams): CheckBox =
            CheckBox(context).apply {
                layoutParams = params
                text = "Retain View Mode"
                setOnCheckedChangeListener { _, isChecked ->
                    retainViewMode = when {
                        isChecked -> RetainViewMode.RETAIN_DETACH
                        else -> RetainViewMode.RELEASE_DETACH
                    }
                }
            }

    private fun pushAnother() {
        router.pushController(RouterTransaction.with(ViewStateSampleController())
                .pushChangeHandler(HorizontalChangeHandler())
                .popChangeHandler(HorizontalChangeHandler()))
    }
}
