package com.esafirm.printer

import com.esafirm.anvildi.DataProvider
import com.esafirm.anvildi.scope.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides

@Module
@ContributesTo(AppScope::class)
class FeaturePrinterModule {

    @Provides
    fun providePrinter(dataProvider: DataProvider): Printer {
        return LoggerDataPrinter(dataProvider)
    }
}

@ContributesTo(AppScope::class)
interface FeaturePrintComponent {
    fun printer(): Printer
}
