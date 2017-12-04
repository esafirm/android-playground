package com.esafirm.androidplayground.conductor.sharedtransition

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.TransitionChangeHandlerCompat
import com.esafirm.androidplayground.R
import com.esafirm.conductorextra.butterknife.BinderController

class SharedTransitionController : BinderController() {

    private val MODELS = listOf(
            SharedItem(R.drawable.res_bird_1, "Burung Satu"),
            SharedItem(R.drawable.res_bird_2, "Burung Dua"),
            SharedItem(R.drawable.res_bird_3, "Burung Tiga")
    )

    @BindView(R.id.share_transition_container) lateinit var container: ViewGroup

    override fun getLayoutResId(): Int = R.layout.controller_share_transition_list

    override fun onViewBound(bindingResult: View, savedState: Bundle?) {
        val inflater = LayoutInflater.from(bindingResult.context)
        MODELS.forEach { item ->
            inflater.inflate(R.layout.item_shared_transition, container, false).let {
                (it.findViewById<ImageView>(R.id.img)).apply {
                    setImageResource(item.imageRes)
                    ViewCompat.setTransitionName(this, "IMAGE${item.imageRes}")
                }

                (it.findViewById<TextView>(R.id.txt)).apply {
                    text = item.text
                    ViewCompat.setTransitionName(this, "TEXT${item.imageRes}")
                }

                val names = listOf("IMAGE${item.imageRes}", "TEXT${item.imageRes}")

                it.setOnClickListener {
                    val push = TransitionChangeHandlerCompat(
                            TestSharedTransitionChangeHandler(names),
                            FadeChangeHandler()
                    )

                    val pop = TransitionChangeHandlerCompat(
                            TestSharedTransitionChangeHandler(names),
                            FadeChangeHandler()
                    )

                    router.pushController(RouterTransaction.with(SharedTransitionDetailController(item))
                            .pushChangeHandler(push)
                            .popChangeHandler(pop))
                }

                container.addView(it)
            }
        }
    }
}
