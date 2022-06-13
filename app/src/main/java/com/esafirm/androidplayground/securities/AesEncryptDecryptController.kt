package com.esafirm.androidplayground.securities

import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.Logger
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.input
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AesEncryptDecryptController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        var inputHolder = ""
        return row {
            input(placeholder = "Hello") {
                inputHolder = it
            }
            button("Encrypt") {
                inputHolder.encrypt()
            }
            button("Decrypt") {
                inputHolder.decrypt()
            }
            logger()
        }
    }

    private fun String.encrypt() {
        val cipher = getCipher(Cipher.ENCRYPT_MODE)

        val encryptedValue = cipher.doFinal(this.toByteArray())
        val encoded = Base64.encodeToString(encryptedValue, Base64.DEFAULT)

        Logger.log("Input: $this")
        Logger.log("Encrypted: $encryptedValue")
        Logger.log("Encoded: $encoded")
    }

    private fun String.decrypt() {
        val cipher = getCipher(Cipher.DECRYPT_MODE)

        val decoded = Base64.decode(this, Base64.DEFAULT)
        val decryptedValue = String(cipher.doFinal(decoded))

        Logger.log("Input: $this")
        Logger.log("Decoded: $decoded")
        Logger.log("Decrypted: $decryptedValue")
    }

    private fun getCipher(mode: Int): Cipher {
        val secretKey = "ABCDABCDABCDABCD"
        val secretKeySpec = SecretKeySpec(secretKey.toByteArray(), "AES")
        val iv = ByteArray(16)
        val charArray = secretKey.toCharArray()

        charArray.forEachIndexed { index, _ ->
            iv[index] = charArray[index].code.toByte()
        }
        val ivParameterSpec = IvParameterSpec(iv)

        return Cipher.getInstance("AES/GCM/NoPadding").apply {
            init(mode, secretKeySpec, ivParameterSpec)
        }
    }
}
