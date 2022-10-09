package com.esafirm.androidplayground.dagger.example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.esafirm.androidplayground.PlaygroundApp
import com.esafirm.androidplayground.dagger.DaggerActivityComponent

import javax.inject.Inject
import javax.inject.Provider

import com.esafirm.androidplayground.libs.Logger.clear
import com.esafirm.androidplayground.libs.Logger.getLogView
import com.esafirm.androidplayground.libs.Logger.log

class DaggerSampleAct : AppCompatActivity() {

    @set:Inject lateinit var classInAppComponent: NonScopedClass
    @set:Inject lateinit var classInAppComponent2: NonScopedClass

    @set:Inject lateinit var scopedClass: ScopedClass
    @set:Inject lateinit var scopedClass2: ScopedClass

    @set:Inject lateinit var singletonClassProvider: Provider<ScopedClass>

    @set:Inject lateinit var scopedInContructorClass: ScopedInClass
    @set:Inject lateinit var scopedInContructorClass2: ScopedInClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerActivityComponent.builder()
            .appComponent(PlaygroundApp.component())
            .build()
            .inject(this)

        clear()
        log(">> Non scoped injected object \n")
        log(classInAppComponent)
        log(classInAppComponent2)
        log(PlaygroundApp.component().classInAppComponent())

        log("\n")
        log(">> Scoped injected object \n")
        log(scopedClass)
        log(scopedClass2)
        log(PlaygroundApp.component().singletonClass())

        log("\n")
        log(">> Scoped with Provider\n")
        log(singletonClassProvider.get()) // Will inject the same thing
        log(singletonClassProvider.get())

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
