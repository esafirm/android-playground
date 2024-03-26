package com.esafirm.androidplayground.anvildi

import com.squareup.anvil.annotations.ContributesTo
import com.squareup.anvil.annotations.MergeComponent
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@ContributesTo(AnvilDIController::class)
@Module
class AnvilControllerModule {
    @Provides
    fun provideContentLoader(controller: AnvilDIController): ContentLoader {
        return ContentLoader(controller.contentId)
    }
}

@MergeComponent(AnvilDIController::class)
interface AnvilControllerComponent {
    fun contentLoader(): ContentLoader

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance controller: AnvilDIController
        ): AnvilControllerComponent
    }
}
