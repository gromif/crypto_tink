package io.gromif.crypto.tink.core

import com.google.crypto.tink.Aead
import com.google.crypto.tink.integration.android.AndroidKeystore
import io.gromif.crypto.tink.encoders.Base64Encoder

class WorkerAeadUtil(
    private val base64Encoder: Base64Encoder,
    private val androidKeysetAlias: String,
    private val associatedData: ByteArray
) {
    private var cachedAead: Aead? = null

    fun generateNewKey() {
        cachedAead = null
        AndroidKeystore.generateNewAes256GcmKey(androidKeysetAlias)
    }

    fun deleteKey() {
        AndroidKeystore.deleteKey(androidKeysetAlias)
    }

    fun encrypt(data: String): String = encryptBytes(data.toByteArray())

    fun encryptBytes(data: ByteArray): String = base64Encoder.encode(
        getAead().encrypt(data, associatedData)
    )

    fun decrypt(data: String): String = decryptBytes(data).decodeToString()

    fun decryptBytes(data: String): ByteArray = getAead().decrypt(
        base64Encoder.decode(data), associatedData
    )

    private fun getAead() = cachedAead ?: AndroidKeystore.getAead(androidKeysetAlias)
        .also { cachedAead = it }
}