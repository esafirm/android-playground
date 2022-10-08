package com.esafirm.anvildi

import com.esafirm.printer.DataProviderImpl
import dagger.Module
import dagger.Provides

@Module
class DataProviderModule {

    @Provides
    fun provideDataProvider(): DataProvider {
        return DataProviderImpl()
    }
}