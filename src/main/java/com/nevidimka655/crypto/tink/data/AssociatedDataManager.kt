package com.nevidimka655.crypto.tink.data

import android.os.Build
import com.google.crypto.tink.prf.PrfSet
import com.google.crypto.tink.subtle.XChaCha20Poly1305
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import java.io.RandomAccessFile
import java.security.SecureRandom
import kotlin.random.Random

class AssociatedDataManager(
    private val associatedDataFile: File
) {
    private val mutex = Mutex()
    private var cached: ByteArray? = null

    suspend fun getAssociatedData() = mutex.withLock {
        cached ?: initAssociatedData().also { cached = it }
    }

    fun decryptWithPassword(password: String, prfSet: PrfSet) {
        val aeadKey = prfSet.computePrimary(password.toByteArray(), AEAD_KEY_SIZE)
        val aeadAssociatedData = prfSet.computePrimary(password.toByteArray(), AEAD_NONCE_SIZE)
        val aead = XChaCha20Poly1305(aeadKey)
        val encryptedBytes = associatedDataFile.readBytes()
        cached = aead.decrypt(encryptedBytes, aeadAssociatedData)
    }

    suspend fun encryptWithPassword(password: String, prfSet: PrfSet) {
        val aeadKey = prfSet.computePrimary(password.toByteArray(), AEAD_KEY_SIZE)
        val aeadAssociatedData = prfSet.computePrimary(password.toByteArray(), AEAD_NONCE_SIZE)
        val aead = XChaCha20Poly1305(aeadKey)
        val encryptedBytes = aead.encrypt(getAssociatedData(), aeadAssociatedData)
        associatedDataFile.writeBytes(encryptedBytes)
    }

    suspend fun decrypt() = associatedDataFile.writeBytes(getAssociatedData())

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
        val newSize = Random.nextInt(from = 16, until = 49)
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