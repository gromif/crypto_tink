package com.nevidimka655.crypto.tink

import android.os.Build
import androidx.collection.SparseArrayCompat
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.Parameters
import com.google.crypto.tink.subtle.AesGcmJce
import com.nevidimka655.crypto.tink.domain.model.AssociatedDataConfig
import com.nevidimka655.crypto.tink.domain.model.keyset.KeysetFactory
import com.nevidimka655.crypto.tink.extensions.aeadPrimitive
import com.nevidimka655.crypto.tink.extensions.sha384
import java.io.File
import java.security.SecureRandom

class KeysetManager(
    private val keysetFactory: KeysetFactory,
    private val associatedDataConfig: AssociatedDataConfig
) {
    private val keysetList = SparseArrayCompat<KeysetHandle>()
    val dataFile get() = associatedDataConfig.dataFile

    private var decodedAssociatedData: ByteArray? = null
    val associatedData
        get() = decodedAssociatedData ?: initAssociatedData(
            ByteArray(associatedDataConfig.dataLength)
        ).also { decodedAssociatedData = it }

    suspend fun aead(aead: KeysetTemplates.AEAD, tag: String = ""): KeysetHandle =
        getKeyset(tag = tag, keyParams = aead.params)

    suspend fun stream(aead: KeysetTemplates.Stream, tag: String = ""): KeysetHandle =
        getKeyset(tag = tag, keyParams = aead.params)

    suspend fun getKeyset(tag: String, keyParams: Parameters): KeysetHandle {
        val keysetHash = tag.hashCode()
        return keysetList[keysetHash] ?: keysetFactory.create(
            tag = tag,
            keyParams = keyParams,
            associatedData = associatedData
        ).also { keysetList.append(keysetHash, it) }
    }

    fun initEncryptedAssociatedData(rawPassword: String) {
        if (dataFile.exists()) {
            val password = HashStringGenerator.extendString(
                rawPassword, associatedDataConfig.dataPasswordHashLength
            )
            val aead = AesGcmJce(password)
            dataFile.inputStream().use {
                val decodedBytes =
                    aead.decrypt(it.readBytes(), dataFile.name.toByteArray())
                if (decodedBytes.size == associatedDataConfig.dataLength) {
                    decodedAssociatedData = decodedBytes
                }
            }
        }
    }

    fun encryptAssociatedData(rawPassword: String) {
        val password = HashStringGenerator.extendString(
            rawPassword, associatedDataConfig.dataPasswordHashLength
        )
        val aead = AesGcmJce(password)
        val byteArray = associatedData
        dataFile.outputStream().use {
            it.write(
                aead.encrypt(byteArray, dataFile.name.toByteArray())
            )
        }
    }

    fun decryptAssociatedData() {
        dataFile.run {
            delete()
            createNewFile()
            outputStream()
        }.use {
            it.write(associatedData)
        }
    }


    private fun initAssociatedData(byteArray: ByteArray): ByteArray {
        if (dataFile.exists()) {
            dataFile.inputStream().use {
                it.read(byteArray)
            }
        } else {
            generateAssociatedData(byteArray)
            dataFile.createNewFile()
            dataFile.outputStream().use {
                it.write(byteArray)
            }
        }
        return byteArray
    }

    fun generateAssociatedData(byteArray: ByteArray) = run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) SecureRandom.getInstanceStrong()
        else SecureRandom()
    }.nextBytes(byteArray)

    fun setAssociatedDataExplicitly(data: ByteArray) {
        decodedAssociatedData = data
    }

    suspend fun transformAssociatedDataToWorkInstance(
        bytesIn: ByteArray,
        encryptionMode: Boolean,
        authenticationTag: String
    ): ByteArray {
        val aead = getKeyset(
            tag = TODO(),
            keyParams = KeysetTemplates.AEAD.AES256_GCM.params
        ).aeadPrimitive()
        return with(aead) {
            val tag = authenticationTag.sha384()
            if (encryptionMode) encrypt(bytesIn, tag)
            else decrypt(bytesIn, tag).also { decodedAssociatedData = it }
        }
    }

}