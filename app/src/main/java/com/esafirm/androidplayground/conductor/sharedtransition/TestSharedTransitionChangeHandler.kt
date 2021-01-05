package com.esafirm.androidplayground.conductor.sharedtransition

import android.os.Bundle
import android.transition.*
import android.view.View
import android.view.ViewGroup


class TestSharedTransitionChangeHandler(var names: List<String>) : SharedElementTransitionChangeHandler() {

    companion object {
        const val KEY_WAITING = "SharedTransition.Waiting"
    }

    constructor() : this(listOf())

    override fun configureSharedElements(container: ViewGroup, from: View?, to: View?, isPush: Boolean) {
        names.forEach {
            addSharedElement(it)
            waitOnSharedElementNamed(it)
        }
    }

    override fun saveToBundle(bundle: Bundle) {
        bundle.putStringArrayList(KEY_WAITING, ArrayList(names))
    }

    override fun restoreFromBundle(bundle: Bundle) {
        names = bundle.getStringArrayList(KEY_WAITING)!!.toList()
    }

    override fun getSharedElementTransition(container: ViewGroup, from: View?, to: View?, isPush: Boolean): Transition? {
        return TransitionSet().apply {
            addTransition(ChangeBounds())
            addTransition(ChangeTransform())
        }
    }

    override fun getEnterTransition(container: ViewGroup, from: View?, to: View?, isPush: Boolean): Transition? {
        return if (isPush) {
            null
        } else {
            null
        }
    }

    override fun getExitTransition(container: ViewGroup, from: View?, to: View?, isPush: Boolean): Transition? {
        return if (isPush) {
            null
        } else Fade().setStartDelay(300)
    }

}
