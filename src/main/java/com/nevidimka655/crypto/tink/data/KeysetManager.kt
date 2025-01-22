package com.nevidimka655.crypto.tink.data

import android.util.SparseArray
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.Parameters
import com.google.crypto.tink.config.TinkConfig
import com.nevidimka655.crypto.tink.domain.KeysetTemplates
import com.nevidimka655.crypto.tink.domain.keyset.KeysetFactory

class KeysetManager(
    private val keysetFactory: KeysetFactory,
    private val associatedDataManager: AssociatedDataManager
) {
    private val keysetList = SparseArray<KeysetHandle>()

    suspend fun aead(aead: KeysetTemplates.AEAD, tag: String = ""): KeysetHandle =
        getKeyset(tag = tag, keyParams = aead.params)

    suspend fun stream(aead: KeysetTemplates.Stream, tag: String = ""): KeysetHandle =
        getKeyset(tag = tag, keyParams = aead.params)

    suspend fun getKeyset(
        tag: String,
        associatedData: ByteArray? = null,
        keyParams: Parameters,
        cache: Boolean = true
    ): KeysetHandle {
        val ad = associatedData ?: associatedDataManager.getAssociatedData()
        val keysetHash = tag.hashCode()
        return keysetList[keysetHash] ?: keysetFactory.create(
            tag = tag,
            keyParams = keyParams,
            associatedData = ad
        ).also { if (cache) keysetList.append(keysetHash, it) }
    }

    suspend fun transformAssociatedDataToWorkInstance(
        bytesIn: ByteArray,
        encryptionMode: Boolean,
        authenticationTag: String
    ): ByteArray {
        return ByteArray(0)
    }

    init {
        TinkConfig.register()
    }

}