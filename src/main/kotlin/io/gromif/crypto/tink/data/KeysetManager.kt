package io.gromif.crypto.tink.data

import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.Parameters
import com.google.crypto.tink.config.TinkConfig
import io.gromif.crypto.tink.model.KeysetFactory
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class KeysetManager(
    private val keysetFactory: KeysetFactory,
    private val associatedDataManager: AssociatedDataManager
) {
    private val mutex = Mutex()
    private val keysetList = HashMap<Int, KeysetHandle>()

    suspend fun getKeyset(
        tag: String,
        associatedData: ByteArray? = null,
        keyParams: Parameters,
        keysetTag: String = "$tag:$keyParams",
        keysetTagHash: Int = keysetTag.hashCode(),
        cache: Boolean = true
    ): KeysetHandle = mutex.withLock {
        val ad = associatedData ?: associatedDataManager.getAssociatedData()

        keysetList[keysetTagHash] ?: keysetFactory.create(
            tag = keysetTag,
            keyParams = keyParams,
            associatedData = ad
        ).also { if (cache) keysetList[keysetTagHash] = it }
    }

    init {
        TinkConfig.register()
    }

}