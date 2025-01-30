package io.gromif.crypto.tink.data

import android.util.SparseArray
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.Parameters
import com.google.crypto.tink.config.TinkConfig
import io.gromif.crypto.tink.domain.KeysetTemplates
import io.gromif.crypto.tink.domain.keyset.KeysetFactory
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class KeysetManager(
    private val keysetFactory: KeysetFactory,
    private val associatedDataManager: AssociatedDataManager
) {
    private val mutex = Mutex()
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
    ): KeysetHandle = mutex.withLock {
        val ad = associatedData ?: associatedDataManager.getAssociatedData()
        val keysetHash = tag.hashCode()

        keysetList[keysetHash] ?: keysetFactory.create(
            tag = tag,
            keyParams = keyParams,
            associatedData = ad
        ).also { if (cache) keysetList.append(keysetHash, it) }
    }

    init {
        TinkConfig.register()
    }

}