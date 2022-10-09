package com.esafirm.androidplayground.securities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.libs.Logger
import com.facebook.android.crypto.keychain.AndroidConceal
import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain
import com.facebook.crypto.CryptoConfig
import com.facebook.crypto.Entity
import java.io.*


class ConcealController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        write(container.context, "Conceal")
        Logger.log(read(container.context))
        return Logger.getLogView(container.context)
    }

    fun write(context: Context, plainText: String) {
        // Creates a new Crypto object with default implementations of a key chain
        val keyChain = SharedPrefsBackedKeyChain(context, CryptoConfig.KEY_256)
        val crypto = AndroidConceal.get().createDefaultCrypto(keyChain)

        // Check for whether the crypto functionality is available
        // This might fail if Android does not load libaries correctly.
        if (!crypto.isAvailable) {
            throw IllegalStateException("No crypto is available")
        }

        val fileStream = BufferedOutputStream(FileOutputStream(
                File(context.cacheDir, "temp")
        ))

        // Creates an output stream which encrypts the data as
        // it is written to it and writes it out to the file.
        val outputStream = crypto.getCipherOutputStream(
                fileStream,
                Entity.create("entity_id")
        )

        // Write plaintext to it.
        outputStream.write(plainText.toByteArray())
        outputStream.close()
    }

    fun read(context: Context): String {
        // Creates a new Crypto object with default implementations of a key chain
        val keyChain = SharedPrefsBackedKeyChain(context, CryptoConfig.KEY_256)
        val crypto = AndroidConceal.get().createDefaultCrypto(keyChain)

        // Get the file to which ciphertext has been written.
        val fileStream = FileInputStream(
                File(context.cacheDir, "temp")
        )

        // Creates an input stream which decrypts the data as
        // it is read from it.
        val inputStream = crypto.getCipherInputStream(
                fileStream,
                Entity.create("entity_id"))

        // Read into a byte array.
        var read: Int = 0
        val buffer = ByteArray(1024)

        val out = ByteArrayOutputStream()
        // You must read the entire stream to completion.
        // The verification is done at the end of the stream.
        // Thus not reading till the end of the stream will cause
        // a security bug. For safety, you should not
        // use any of the data until it's been fully read or throw
        // away the data if an exception occurs.
        while ({ read = inputStream.read(buffer); read }.invoke() != -1) {
            out.write(buffer, 0, read)
        }

        inputStream.close()

        return String(out.toByteArray())
    }
}
