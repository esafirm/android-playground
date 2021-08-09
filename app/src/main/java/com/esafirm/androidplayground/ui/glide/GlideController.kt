package com.esafirm.androidplayground.ui.glide

import android.os.Debug
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.Request
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.dp
import com.esafirm.androidplayground.utils.matchParent
import com.esafirm.androidplayground.utils.row
import com.esafirm.androidplayground.utils.switch
import kotlin.random.Random

class GlideController : BaseController() {

    private var imageView: ImageView? = null
    private var useCache: Boolean = false

    private val requestInfoPasser = RequestInfoPasser()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            setPadding(16.dp.toInt())
            switch("Use cache") { isChecked ->
                useCache = isChecked
            }
            button("Request") {
                imageView?.requestImage()
            }
            button("Request with Measure") {
                imageView?.requestImageWithMeasure()
            }

            button("Request with cache") {
                val img = imageView!!
                val id = Random.nextInt()

                val target = CallbackImageViewTarget(img) { req ->
                    if (req == null) error("Request is null")
                    val info = requestInfoPasser.getInfo(req)
                    if (info == null) {
                        Log.d("Glide", "Callback info null with id $id")
                    } else {
                        Log.d("Glide", "Callback with id $info")
                    }
                }

                target.request?.let { req ->
                    requestInfoPasser.setInfo(req, id)
                }

                Glide.with(img)
                    .load("https://s-light.tiket.photos/t/01E25EBZS3W0FY9GTG6C42E1SE/rsfit500500gsm/mobile-modules/2021/07/05/84b94d79-4a46-47a5-9cc1-caf129191fc1-1625495902018-f425ad5f10accdce8e3c2c561cde9ff9.png")
                    .into(target)

                Log.d("Glide", "Actual id: $id")
            }

            button("Request without image loader - Tracing") {
                Debug.startMethodTracing("Load image")
                val img = imageView!!
                Glide.with(img)
                    .load("https://s-light.tiket.photos/t/01E25EBZS3W0FY9GTG6C42E1SE/rsfit500500gsm/mobile-modules/2021/07/05/84b94d79-4a46-47a5-9cc1-caf129191fc1-1625495902018-f425ad5f10accdce8e3c2c561cde9ff9.png")
                    .into(CallbackImageViewTarget(img) {
                        Debug.stopMethodTracing()
                    })
            }
            imageView = addImageView()
        }
    }

    private fun ViewGroup.addImageView(): ImageView {
        val imageView = ImageView(context).apply {
            matchParent(vertical = false)
        }
        addView(imageView)
        return imageView
    }
}

class RequestInfoPasser() {
    private val backingMap = mutableMapOf<Request, Int>()
    fun setInfo(request: Request, info: Int) {
        backingMap[request] = info
    }

    fun getInfo(request: Request): Int? {
        val info = backingMap[request]
        backingMap.remove(request)
        return info
    }
}