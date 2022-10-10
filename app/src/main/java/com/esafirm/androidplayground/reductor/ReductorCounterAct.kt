package com.esafirm.androidplayground.reductor

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.esafirm.androidplayground.common.BaseAct
import com.esafirm.androidplayground.libs.Logger
import com.yheriatovych.reductor.Actions
import com.yheriatovych.reductor.Store

class ReductorCounterAct : BaseAct() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val counterStore = Store.create(CounterReducer.create())
        val actions = Actions.from(CounterActions::class.java)

        counterStore.subscribe { integer ->
            Logger.clear()
            Logger.log(integer)
        }

        counterStore.dispatch(actions.increment()) //print 1
        counterStore.dispatch(actions.increment()) //print 2
        counterStore.dispatch(actions.add(5))

        val button = Button(this)
        button.text = "Add Random"
        button.setOnClickListener { counterStore.dispatch(actions.add((Math.random() * 10).toInt())) }
        button.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val linearLayout = LinearLayout(this)
        linearLayout.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(button)
        linearLayout.addView(Logger.getLogView(this))

        setContentView(linearLayout)
    }
}
