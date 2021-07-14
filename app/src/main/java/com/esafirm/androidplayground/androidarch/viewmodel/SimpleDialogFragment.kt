package com.esafirm.androidplayground.androidarch.viewmodel

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.esafirm.androidplayground.common.BaseDialogFragment
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row

class SimpleDialogFragment : BaseDialogFragment() {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(DataHolderViewModel::class.java)
    }

    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        textView = TextView(requireContext())

        return row {
            setPadding(32, 32, 32, 32)
            addView(textView)
            logger()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        viewModel.dataStream.observe(viewLifecycleOwner) { data ->
            textView.text = """
                Is restore state: ${viewModel.isRestoreState}
                Data: $data
            """.trimIndent()
        }
    }
}