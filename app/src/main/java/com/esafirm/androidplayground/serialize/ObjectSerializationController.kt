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
data class ObjectWithDefault(
    val a: String = "a",
    val b: String? = "b"
)

class ObjectSerializationController : BaseController() {

    private val adapter = KotlinJsonAdapterFactory()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            button("Serialize") {
                val defaultObj = ObjectWithDefault("abc ")
                val result = adapter.of(ObjectWithDefault.serializer()).to(defaultObj)
                Logger.log("default obj a:${defaultObj.a} b:${defaultObj.b}")
                Logger.log(result)
            }
            button("Deserialize") {
                val json = """
                    { "a": "abc" }
                """.trimIndent()

                Logger.log(adapter.of(ObjectWithDefault.serializer()).from(json))
            }
            logger()
        }
    }
}

