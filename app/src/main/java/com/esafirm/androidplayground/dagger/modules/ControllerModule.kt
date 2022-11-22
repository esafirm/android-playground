package com.esafirm.androidplayground.dagger.modules

import com.esafirm.androidplayground.dagger.ControllerScope
import dagger.Module
import dagger.Provides
import javax.inject.Named
import kotlin.random.Random

@Module
class ControllerModule {

    /**
     * If this changed to different scope that is not matching with its component
     * (in this case [com.esafirm.androidplayground.dagger.ControllerComponent]
     * This will result in compile error
     */
    @ControllerScope
    @Named("controller")
    @Provides
    fun provideControllerString(): String {
        return "Controller string -- ${Random.nextInt()}"
    }
}
