package com.esafirm.androidplayground.utils

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec


enum class Lifecycle {
    CREATED,
    DESTROYED
}

interface LifecycleObserver {
    fun update(lifecycle: Lifecycle)
    fun observe(onUpdate: (Lifecycle) -> Unit)
}

object Forever : LifecycleObserver {
    override fun update(lifecycle: Lifecycle) {
    }

    override fun observe(onUpdate: (Lifecycle) -> Unit) {
    }
}


interface ObservableState<T> {
    val currentValue: T

    fun setValue(value: T)
    fun observe(lifecycleObserver: LifecycleObserver?, onUpdate: (T) -> Unit)
}

class CombineObservable<T>(
    private vararg val observables: ObservableState<T>
) : ObservableState<T> {

    override val currentValue: T
        get() = observables.first().currentValue

    override fun setValue(value: T) {
        observables.forEach { it.setValue(value) }
    }

    override fun observe(lifecycleObserver: LifecycleObserver?, onUpdate: (T) -> Unit) {
        observables.forEach {
            it.observe(lifecycleObserver, onUpdate)
        }
    }
}

class SimpleObservableState<T> : ObservableState<T> {

    private var holder: T? = null
    private val observers = mutableListOf<(T) -> Unit>()

    override val currentValue: T
        get() = holder!!

    override fun setValue(value: T) {
        holder = value
        observers.forEach {
            it.invoke(value)
        }
    }

    override fun observe(lifecycleObserver: LifecycleObserver?, onUpdate: (T) -> Unit) {
        observers.add(onUpdate)

        lifecycleObserver?.observe {
            if (it == Lifecycle.DESTROYED) {
                observers.remove(onUpdate)
            }
        }
    }
}

class ObservableTest : StringSpec({
    "Current value should reflect set value" {
        val obs = SimpleObservableState<String>()
        obs.setValue("A")
        obs.currentValue shouldBe "A"
    }

    "Observer should get update from set value" {
        val obs = SimpleObservableState<String>()
        lateinit var currentValue: String
        obs.observe(Forever) {
            currentValue = it
        }
        obs.setValue("A")

        currentValue shouldBe "A"
    }

    "Combine observable should working correctly" {
        val obs1 = SimpleObservableState<String>()
        val obs2 = SimpleObservableState<String>()

        obs1.setValue("A")

        val combined = CombineObservable(obs1, obs2)

        combined.currentValue shouldBe "A"

        lateinit var result: String
        combined.observe(Forever) {
            result = it
        }

        obs1.setValue("A")
        obs2.setValue("B")

        result shouldBe "B"
    }

})