package com.esafirm.androidplayground.ui.nestedscroll

import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import butterknife.BindView
import com.esafirm.androidplayground.R
import com.esafirm.conductorextra.butterknife.BinderController

class NestedScrollingController : BinderController() {

    @BindView(R.id.rv) lateinit var recycler: RecyclerView
    @BindView(R.id.appbar) lateinit var appBar: AppBarLayout

    override fun getLayoutResId(): Int = R.layout.controller_nested_scroll_test

    override fun onViewBound(bindingResult: View, savedState: Bundle?) {
        val adapter = TestAdapter(bindingResult.context)
        recycler.layoutManager = LinearLayoutManager(bindingResult.context, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = adapter

        appBar.layoutParams.let { it as CoordinatorLayout.LayoutParams }
                .behavior = FixAppBarLayoutBehavior()
    }
}
