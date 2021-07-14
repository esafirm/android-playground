package com.esafirm.androidplayground.common

import android.content.Context
import androidx.fragment.app.DialogFragment
import com.esafirm.androidplayground.utils.ContextProvider

open class BaseDialogFragment : DialogFragment(), ContextProvider {

    override val requiredContext: Context
        get() = requireActivity()
}