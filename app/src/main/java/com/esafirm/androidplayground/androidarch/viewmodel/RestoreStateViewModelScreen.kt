package com.esafirm.androidplayground.androidarch.viewmodel

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.esafirm.androidplayground.R
import com.esafirm.androidplayground.common.BaseAct
import com.esafirm.androidplayground.utils.Logger
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row
import java.io.ByteArrayOutputStream

class RestoreStateViewModelScreen : BaseAct() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(DataHolderViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(row {
            button("Set Data") { setData(requiredContext) }
            button("Navigate") { start(this@RestoreStateViewModelScreen) }
            logger()
        })

        viewModel.dataStream.observe(this) { data ->
            Logger.log("Data in ViewHolder: ${data.length}")
        }
        Logger.log("Is restored: ${viewModel.isRestoreState}")
    }

    private fun setData(context: Context) {
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.res_bird_1)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

        viewModel.setData("TEST", outputStream.toString(Charsets.UTF_8.name()))

        SimpleDialogFragment().show(
            supportFragmentManager,
            SimpleDialogFragment::class.java.simpleName
        )
    }

    companion object {
        fun start(context: Context) {
            Intent(context, RestoreStateViewModelScreen::class.java).also {
                context.startActivity(it)
            }
        }
    }
}