package com.esafirm.printer

import com.esafirm.anvildi.DataProvider
import com.esafirm.anvildi.scope.AppScope
import com.squareup.anvil.annotations.ContributesTo

class DataProviderImpl : DataProvider {
    override fun provideData(): Any {
        return "This is data from ${System.currentTimeMillis()}"
    }
}
