package com.esafirm.printer

import com.esafirm.anvildi.DataProvider

class DataPrinter(
    private val dataSource: DataProvider
) : Printer {
    override fun print() {
        println(dataSource.provideData())
    }
}