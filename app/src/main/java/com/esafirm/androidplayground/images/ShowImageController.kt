package com.esafirm.androidplayground.images

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.R
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.image
import com.esafirm.androidplayground.utils.scroll

class ShowImageController : BaseController() {
    override fun onCreateView(p0: LayoutInflater, p1: ViewGroup): View {
        return scroll {
            image("http://www.tripelangi.com/tgn_draft/svc/image_laporan/laporan22_foto_19031766265c00d20f982c4.jpg")
            image("https://picsum.photos/200/300")
            image("https://picsum.photos/200/300")
            image(
                imageUrl = "http://www.tripelangi.com/tgn_draft/svc/image_laporan/laporan22_foto_19031766265c00d20f982c4.jpg",
                placeholder = R.drawable.ic_copyright
            )
        }
    }
}