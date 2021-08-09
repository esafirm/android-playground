package com.esafirm.androidplayground.ui.glide

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.recyclerView
import com.esafirm.androidplayground.utils.row
import com.esafirm.androidplayground.utils.switch

class GlideListController : BaseController() {

    private var useOnPreDraw: Boolean = false
    private var adapter: GlideSampleAdapter = GlideSampleAdapter()

    private val items by lazy {
        (0 until 50).map {
            if (it % 2 == 0) {
                GlideItem(isAvatar = false, "")
            } else {
                GlideItem(isAvatar = true, "")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            switch("Use onPreDraw") { isChecked ->
                useOnPreDraw = isChecked
            }
            button("Attach") {
                GlideRequestTrackerStore.clear()
                adapter.setup(items, useOnPreDraw)
            }
            button("Print result") {
                GlideRequestTrackerStore.printResult()
            }
            recyclerView(passedAdapter = adapter)
        }
    }
}