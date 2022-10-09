package com.esafirm.anvildi

import com.esafirm.anvildi.scope.AppScope
import com.esafirm.printer.DataProviderImpl
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides

@Suppress("unused")
@Module
@ContributesTo(AppScope::class)
class DataProviderModule {

    @Provides
    fun provideDataProvider(): DataProvider {
        return DataProviderImpl()
    }
}
