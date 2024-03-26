package com.esafirm.androidplayground.serialize

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.libs.Logger
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row
import kotlinx.serialization.Serializable

@Serializable
private data class ObjectWithDefault(
    val a: String = "a",
    val b: String? = "b"
)

private interface UniqueItem {
    val id: String
}

@Serializable
private data class Invite(
    val inviteId: String = "",
    override val id: String
) : UniqueItem

@Serializable
private data class SmartInvite(
    val inviteId: String = "",
    override val id: String = inviteId
) : UniqueItem

@Serializable
private class ClassWithDelegateObject(
    val id: String
) {
    val description by lazy(LazyThreadSafetyMode.NONE) {
        "A lazy description"
    }
}

class ObjectSerializationController : BaseController() {

    private val adapter = KotlinJsonAdapterFactory()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            addSerializeButton()
            addDeserializeButton()
            addDeserializeWithDefaultValues()
            addDeserializeWithDelegateField()
            logger()
        }
    }

    private fun ViewGroup.addDeserializeWithDelegateField() {
        button("Delegate filed behavior") {
            val serializer = adapter.of(ClassWithDelegateObject.serializer())

            try {
                val json = """{ "id": "123123123AAA", "description": "Description from JSON" }"""
                Logger.log(serializer.from(json))
            } catch (e: Exception) {
                Logger.log(e)
            }

            val obj = ClassWithDelegateObject(id = "ID_FROM_CONSTRUCTOR")
            val result = serializer.to(obj)

            Logger.log("Serialize: $result")

            Logger.log("Accessing description: ${obj.description}")
            Logger.log("Serialize again: ${serializer.to(obj)}")
        }
    }

    private fun ViewGroup.addDeserializeWithDefaultValues() {
        button("Deserialize Default Value #1") {

            val json = """{ "id": "123123123AAA" }"""
            Logger.log(adapter.of(Invite.serializer()).from(json))
        }

        button("Deserialize Default Value #2") {

            val json = """{ "inviteId": "123123123AAA" }"""
            Logger.log(adapter.of(SmartInvite.serializer()).from(json))
        }
    }

    private fun ViewGroup.addDeserializeButton() {
        button("Deserialize") {

            val json = """{ "a": "abc" }"""
            Logger.log(adapter.of(ObjectWithDefault.serializer()).from(json))
        }
    }

    private fun ViewGroup.addSerializeButton() {
        button("Serialize") {
            val defaultObj = ObjectWithDefault("abc ")
            val result = adapter.of(ObjectWithDefault.serializer()).to(defaultObj)
            Logger.log("default obj a:${defaultObj.a} b:${defaultObj.b}")
            Logger.log(result)
        }
    }

}
