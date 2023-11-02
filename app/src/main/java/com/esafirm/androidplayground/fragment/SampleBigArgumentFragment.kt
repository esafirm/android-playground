package com.esafirm.androidplayground.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.esafirm.androidplayground.utils.ContextProvider
import com.esafirm.androidplayground.utils.row
import com.esafirm.androidplayground.utils.text

/**
 * A [Fragment] that has big argument to test [TransactionTooLargeException]
 */
class SampleBigArgumentFragment : Fragment(), ContextProvider {

    override val requiredContext: Context
        get() = requireActivity()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return row {
            text("This is a Fragment with big argument")
        }
    }

    companion object {

        private const val KEY_ARGUMENT = "Key.Argument"

        // Create a 10 mb string
        private val bigString: String
            get() {
                return buildString {
                    repeat(10000000) {
                        append("A")
                    }
                }
            }

        fun newInstance(): SampleBigArgumentFragment {
            val bundle = Bundle().apply {
                putString(KEY_ARGUMENT, bigString)
            }
            return SampleBigArgumentFragment().apply {
                arguments = bundle
            }
        }
    }
}
