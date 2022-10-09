package com.esafirm.printer

import com.esafirm.androidplayground.libs.Logger
import com.esafirm.anvildi.DataProvider

class LoggerDataPrinter(
    private val dataSource: DataProvider,
) : Printer {
    override fun print() {
        Logger.log(dataSource.provideData())
    }
}
