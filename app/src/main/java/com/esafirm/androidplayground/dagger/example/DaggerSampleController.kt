package com.esafirm.androidplayground.dagger.example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.PlaygroundApp
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.dagger.ControllerScope
import com.esafirm.androidplayground.dagger.DaggerControllerComponent
import com.esafirm.androidplayground.dagger.RandomStringProvider
import com.esafirm.androidplayground.libs.Logger.clear
import com.esafirm.androidplayground.libs.Logger.log
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row
import javax.inject.Inject
import javax.inject.Provider
import kotlin.random.Random

class DaggerSampleController : BaseController() {

    @set:Inject
    lateinit var classInAppComponent: NonScopedClass

    @set:Inject
    lateinit var classInAppComponent2: NonScopedClass

    @set:Inject
    lateinit var scopedClass: ScopedClass

    @set:Inject
    lateinit var scopedClass2: ScopedClass

    @set:Inject
    lateinit var singletonClassProvider: Provider<ScopedClass>

    @set:Inject
    lateinit var scopedInContructorClass: ScopedInClass

    @set:Inject
    lateinit var scopedInContructorClass2: ScopedInClass

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        runDagger()
        return row {
            logger()
        }
    }

    private fun runDagger() {
        val component = DaggerControllerComponent.builder()
            .appComponent(PlaygroundApp.component())
            .randomStringProvider(object : RandomStringProvider {

                @ControllerScope
                override fun randomString(): String {
                    return "String #${Random.nextInt()}"
                }
            })
            .build()

        component.inject(this)

        clear()
        log(">> Non scoped injected object \n")
        log(classInAppComponent)
        log(classInAppComponent2)
        log(PlaygroundApp.component().classInAppComponent())

        log("\n>> Scoped injected object -- Singleton\n")
        log(scopedClass)
        log(scopedClass2)
        log(PlaygroundApp.component().singletonClass())

        log("\n>> Scoped with Provider -- ControllerScope\n")
        log(singletonClassProvider.get()) // Will inject the same thing
        log(singletonClassProvider.get())

        log("\n>> Scoped in constructor -- ControllerScope\n")
        log(scopedInContructorClass)
        log(scopedInContructorClass2)

        log("\n>> Scoped in Module -- ControllerScope \n")
        log(component.controllerString())
        log(component.controllerString())

        log("\n>> Dependency from non-dagger Interface -- ControllerScope \n")
        log("As you can see scoping will not work on the impl of interface\n")
        log("It will follow the real implementation behavior\n")
        log(component.randomString())
        log(component.randomString())
    }
}
