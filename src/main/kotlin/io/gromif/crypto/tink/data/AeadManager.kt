package io.gromif.crypto.tink.data

import com.google.crypto.tink.Aead
import com.google.crypto.tink.DeterministicAead
import com.google.crypto.tink.StreamingAead
import com.google.crypto.tink.aead.AeadParameters
import com.google.crypto.tink.daead.DeterministicAeadParameters
import com.google.crypto.tink.prf.PrfParameters
import com.google.crypto.tink.prf.PrfSet
import com.google.crypto.tink.streamingaead.StreamingAeadParameters
import io.gromif.crypto.tink.extensions.aead
import io.gromif.crypto.tink.extensions.deterministicAead
import io.gromif.crypto.tink.extensions.prf
import io.gromif.crypto.tink.extensions.streamingAead

class AeadManager(
    private val keysetManager: KeysetManager
) {
    private val aeadMap = HashMap<Int, Aead>()
    private val prfMap = HashMap<Int, PrfSet>()
    private val deterministicAeadMap = HashMap<Int, DeterministicAead>()
    private val streamingAeadMap = HashMap<Int, StreamingAead>()

    suspend fun aead(
        tag: String,
        associatedData: ByteArray? = null,
        keyParams: AeadParameters,
    ): Aead {
        val tagHash = tag.hashCode()
        return aeadMap[tagHash] ?: keysetManager.getKeyset(
            tag = tag,
            tagHash = tagHash,
            associatedData = associatedData,
            keyParams = keyParams,
            cache = false
        ).aead().also {
            aeadMap[tagHash] = it
        }
    }

    suspend fun prf(
        tag: String,
        associatedData: ByteArray? = null,
        keyParams: PrfParameters,
    ): PrfSet {
        val tagHash = tag.hashCode()
        return prfMap[tagHash] ?: keysetManager.getKeyset(
            tag = tag,
            tagHash = tagHash,
            associatedData = associatedData,
            keyParams = keyParams,
            cache = false
        ).prf().also {
            prfMap[tagHash] = it
        }
    }

    suspend fun deterministicAead(
        tag: String,
        associatedData: ByteArray? = null,
        keyParams: DeterministicAeadParameters,
    ): DeterministicAead {
        val tagHash = tag.hashCode()
        return deterministicAeadMap[tagHash] ?: keysetManager.getKeyset(
            tag = tag,
            tagHash = tagHash,
            associatedData = associatedData,
            keyParams = keyParams,
            cache = false
        ).deterministicAead().also {
            deterministicAeadMap[tagHash] = it
        }
    }

    suspend fun streamingAead(
        tag: String,
        associatedData: ByteArray? = null,
        keyParams: StreamingAeadParameters,
    ): StreamingAead {
        val tagHash = tag.hashCode()
        return streamingAeadMap[tagHash] ?: keysetManager.getKeyset(
            tag = tag,
            tagHash = tagHash,
            associatedData = associatedData,
            keyParams = keyParams,
            cache = false
        ).streamingAead().also {
            streamingAeadMap[tagHash] = it
        }
    }

}