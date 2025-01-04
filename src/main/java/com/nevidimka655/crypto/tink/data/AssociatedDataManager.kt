package com.nevidimka655.crypto.tink.data

import android.os.Build
import com.google.crypto.tink.prf.PrfSet
import com.google.crypto.tink.subtle.XChaCha20Poly1305
import java.io.File
import java.io.RandomAccessFile
import java.security.SecureRandom
import kotlin.random.Random

class AssociatedDataManager(
    private val associatedDataFile: File
) {
    private var cached: ByteArray? = null
    val associatedData get() = cached ?: initAssociatedData().also { cached = it }

    fun decryptWithPassword(password: String, prfSet: PrfSet) {
        val aeadKey = prfSet.computePrimary(password.toByteArray(), AEAD_KEY_SIZE)
        val aeadAssociatedData = prfSet.computePrimary(password.toByteArray(), AEAD_NONCE_SIZE)
        val aead = XChaCha20Poly1305(aeadKey)
        val encryptedBytes = associatedDataFile.readBytes()
        cached = aead.decrypt(encryptedBytes, aeadAssociatedData)
    }

    fun encryptWithPassword(password: String, prfSet: PrfSet) {
        val aeadKey = prfSet.computePrimary(password.toByteArray(), AEAD_KEY_SIZE)
        val aeadAssociatedData = prfSet.computePrimary(password.toByteArray(), AEAD_NONCE_SIZE)
        val aead = XChaCha20Poly1305(aeadKey)
        val encryptedBytes = aead.encrypt(associatedData, aeadAssociatedData)
        associatedDataFile.writeBytes(encryptedBytes)
    }

    fun decrypt() = associatedDataFile.writeBytes(associatedData)

    fun setExplicitly(data: ByteArray) {
        cached = data
    }

    fun erase() = RandomAccessFile(associatedDataFile, "rws").use { it.write(generate()) }

    private fun initAssociatedData(): ByteArray {
        return if (associatedDataFile.exists()) associatedDataFile.readBytes()
        else {
            val bytes = generate()
            associatedDataFile.createNewFile()
            associatedDataFile.writeBytes(bytes)
            bytes
        }
    }

    private fun generate(): ByteArray {
        val newSize = Random.nextInt(from = 32, until = 65)
        val bytes = ByteArray(newSize)
        run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) SecureRandom.getInstanceStrong()
            else SecureRandom()
        }.nextBytes(bytes)
        return bytes
    }

}

private const val AEAD_KEY_SIZE = 32
private const val AEAD_NONCE_SIZE = 24