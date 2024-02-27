package com.esafirm.androidplayground.ui.attach

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.ui.nestedrecyclerview.AbstractSingleItem
import com.esafirm.androidplayground.ui.nestedrecyclerview.AdapterItem
import com.esafirm.androidplayground.ui.nestedrecyclerview.CommonAdapter
import com.esafirm.androidplayground.utils.recyclerView
import com.esafirm.androidplayground.utils.row

class AttachTestController : BaseController() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val items = mutableListOf<AdapterItem>()

        repeat(100) { pos ->
            items.add(AbstractSingleItem(
                text = "item $pos",
                extraCreate = {
                    it.textView.setOnAttach(
                        onAttach = { Log.d("R", "onAttach: ${it.adapterPosition}") },
                        onDetach = { Log.d("R", "onDetach: ${it.adapterPosition}") }
                    )
                }
            ))
        }

        return row {
            recyclerView(
                passedAdapter = CommonAdapter(items)
            )
        }
    }
}

private fun View.setOnAttach(
    onAttach: () -> Unit,
    onDetach: () -> Unit
) {
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {
            onAttach()
        }

        override fun onViewDetachedFromWindow(v: View) {
            onDetach()
        }
    })
}
