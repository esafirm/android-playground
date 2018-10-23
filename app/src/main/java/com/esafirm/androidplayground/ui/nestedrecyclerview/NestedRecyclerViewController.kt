package com.esafirm.androidplayground.ui.nestedrecyclerview

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import butterknife.BindView
import com.esafirm.androidplayground.R
import com.esafirm.conductorextra.butterknife.BinderController

class NestedRecyclerViewController : BinderController {

    @BindView(R.id.recyclerView) lateinit var recyclerView: RecyclerView

    constructor() : super()
    constructor(bundle: Bundle) : super(bundle)

    override fun getLayoutResId(): Int = R.layout.controller_nested_recyclerview

    override fun onViewBound(bindingResult: View, savedState: Bundle?) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = NestedRecyclerAdapter().apply {
                newsItems.addAll(createItems())
            }
        }
    }

    private fun createItems() = listOf(
            HeadlineNewsItem(listOf("Headline 1", "Headline 2", "Headline 3", "Headline 4", "Headline 5", "Headline 1", "Headline 2", "Headline 3", "Headline 4", "Headline 5")),
            SingleNewsItem("This is a news 1"),
            SingleNewsItem("This is a news 1"),
            SingleNewsItem("This is a news 1"),
            SingleNewsItem("This is a news 1"),
            SingleNewsItem("This is a news 1"),
            SingleNewsItem("This is a news 1"),
            SingleNewsItem("This is a news 1"),
            SingleNewsItem("This is a news 1"),
            SingleNewsItem("This is a news 1"),
            SingleNewsItem("This is a news 1"),
            SingleNewsItem("This is a news 10"),
            SingleNewsItem("This is a news 1"),
            SingleNewsItem("This is a news 1"),
            SingleNewsItem("This is a news 10"),
            SingleNewsItem("This is a news 1"),
            SingleNewsItem("This is a news 1"),
            SingleNewsItem("This is a news 10")
    )

}
