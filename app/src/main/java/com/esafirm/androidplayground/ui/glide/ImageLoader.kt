package com.esafirm.androidplayground.ui.glide

import android.widget.ImageView
import androidx.core.view.OneShotPreDrawListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

private const val IMAGE_URL =
    "https://s-light.tiket.photos/t/01E25EBZS3W0FY9GTG6C42E1SE/original/mobile-modules/2021/07/05/84b94d79-4a46-47a5-9cc1-caf129191fc1-1625495902018-f425ad5f10accdce8e3c2c561cde9ff9.png"
private const val IMAGE_URL_SMALL =
    "https://s-light.tiket.photos/t/01E25EBZS3W0FY9GTG6C42E1SE/rsfit500500gsm/mobile-modules/2021/07/05/84b94d79-4a46-47a5-9cc1-caf129191fc1-1625495902018-f425ad5f10accdce8e3c2c561cde9ff9.png"
private const val IMAGE_PICSUM = "https://picsum.photos/200/300"

internal fun ImageView.requestImage(useCache: Boolean = false) {
    val tracker = GlideRequestTracker.start()

    Glide.with(this)
        .load(IMAGE_URL)
        .apply {
            if (useCache.not()) {
                diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
            }
        }
        .into(CallbackImageViewTarget(this) {
            tracker.log(EventType.WHOLE)
        })
}

internal fun ImageView.requestImageWithMeasure(useCache: Boolean = false) {
    val tracker = GlideRequestTracker.start()

    val sizeIsAvailable = width > 0
    if (!sizeIsAvailable) {
        requestImage(tracker, useCache)
        return
    }

    OneShotPreDrawListener.add(this) {
        tracker.log(EventType.PREDRAW)
        requestImage(tracker, useCache)
    }
}

internal fun ImageView.requestImage(tracker: GlideRequestTracker, useCache: Boolean) {
    Glide.with(this)
        .load(IMAGE_URL_SMALL)
        .apply {
            if (useCache.not()) {
                diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
            }
        }
        .into(CallbackImageViewTarget(this) {
            tracker.log(EventType.WHOLE)
        })
}

private fun normalizeSize(size: Int): Int {
    return (size / 100) * 100
}
