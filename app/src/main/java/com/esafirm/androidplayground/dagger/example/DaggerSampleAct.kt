package com.esafirm.androidplayground.dagger.example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.esafirm.androidplayground.App
import com.esafirm.androidplayground.dagger.DaggerActivityComponent

import javax.inject.Inject
import javax.inject.Provider

import com.esafirm.androidplayground.utils.Logger.clear
import com.esafirm.androidplayground.utils.Logger.getLogView
import com.esafirm.androidplayground.utils.Logger.log

class DaggerSampleAct : AppCompatActivity() {

    @Inject internal var classInAppComponent: NonScopedClass? = null
    @Inject internal var classInAppComponent2: NonScopedClass? = null

    @Inject internal var scopedClass: ScopedClass? = null
    @Inject internal var scopedClass2: ScopedClass? = null

    @Inject internal var singletonClassProvider: Provider<ScopedClass>? = null

    @Inject internal var scopedInContructorClass: ScopedInClass? = null
    @Inject internal var scopedInContructorClass2: ScopedInClass? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerActivityComponent.builder()
            .appComponent(App.component())
            .build()
            .inject(this)

        clear()
        log(">> Non scoped injected object \n")
        log(classInAppComponent) // Wi
        log(classInAppComponent2)
        log(App.component().classInAppComponent())

        log("\n")
        log(">> Scoped injected object \n")
        log(scopedClass)
        log(scopedClass2)
        log(App.component().singletonClass())

        log("\n")
        log(">> Scoped with Provider\n")
        log(singletonClassProvider!!.get()) // Will inject the same thing
        log(singletonClassProvider!!.get())

        log("\n")
        log(">> Scoped in constructor\n")
        log(scopedInContructorClass)
        log(scopedInContructorClass2)

        setContentView(getLogView(this))
    }

    companion object {

        /* --------------------------------------------------- */
        /* > Stater */
        /* --------------------------------------------------- */

        fun start(context: Context) {
            context.startActivity(Intent(context, DaggerSampleAct::class.java))
        }
    }
}
