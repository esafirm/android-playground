package com.esafirm.androidplayground.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.esafirm.androidplayground.R
import com.esafirm.androidplayground.common.BaseAct
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.matchParent
import com.esafirm.androidplayground.utils.row


class FragmentPlaygroundActivity : BaseAct() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(row {
            matchParent(vertical = false)

            button("ViewPager Sample") {
                runViewPagerSample()
            }
            button("Fragment TransactionTooLarge") {
                runFragmentTransactionTooLargeSample()
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        print("Save instance state called: $outState -- size: ${outState.size()}")
        super.onSaveInstanceState(outState)
    }

    private fun runFragmentTransactionTooLargeSample() {
        val frame = FrameLayout(this).apply {
            matchParent()
            id = R.id.content
        }
        setContentView(frame)

        supportFragmentManager.beginTransaction()
            .add(R.id.content, SampleBigArgumentFragment.newInstance(), "SampleBigArgumentFragment")
            .commit()
    }

    private fun runViewPagerSample() {
        setContentView(R.layout.activity_current_fragment)

        val viewPager2 = findViewById<ViewPager2>(R.id.viewPager)
        viewPager2.adapter = SampleAdapter(this)

        val pageMargin = resources.getDimensionPixelOffset(R.dimen.offset).toFloat()
        val pageOffset = resources.getDimensionPixelOffset(R.dimen.offset).toFloat()

        viewPager2.setPageTransformer { page, position ->
            val myOffset = position * -(2 * pageOffset + pageMargin)
            if (viewPager2.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(viewPager2) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -myOffset
                } else {
                    page.translationX = myOffset
                }
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, FragmentPlaygroundActivity::class.java))
        }
    }
}


class SampleFragment : Fragment(R.layout.fragment_current_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val textView = view.findViewById<TextView>(R.id.text).apply {
            text = "Click Me!"
        }
        textView.setOnClickListener {
            val fragments = requireActivity().supportFragmentManager.fragments
            val hostFragments = fragments.joinToString { "$it" }
            val index = fragments.indexOf(this)
            val currentVisible = fragments.first { it.isVisible }
            val isVisible = currentVisible == this
            val text = """
                Current fragment:
                $this
                
                Fragments:
                $hostFragments
                
                Current Visible:
                $currentVisible
                
                Is Current Visible: $isVisible 
                size: ${fragments.size}
                index: $index
            """.trimIndent()

            textView.text = text
        }
    }
}

class SampleAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 10

    override fun createFragment(position: Int): Fragment {
        return SampleFragment()
    }
}
