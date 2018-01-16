package com.esafirm.androidplayground.ui.nestedscroll

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import com.esafirm.androidplayground.R
import com.esafirm.conductorextra.butterknife.BinderController

class NestedScrollingController : BinderController() {

    @BindView(R.id.rv) lateinit var recycler: RecyclerView

    override fun getLayoutResId(): Int = R.layout.controller_nested_scroll_test

    override fun onViewBound(bindingResult: View, savedState: Bundle?) {
        val adapter = TestAdapter(bindingResult.context)
        recycler.layoutManager = LinearLayoutManager(bindingResult.context, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = adapter
        LinearSnapHelper().attachToRecyclerView(recycler)
    }
}
