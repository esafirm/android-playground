package com.esafirm.printer

import com.esafirm.anvildi.DataProvider

class DataProviderImpl : DataProvider {
    override fun provideData(): Any {
        return "This is data from ${System.currentTimeMillis()}"
    }
}
