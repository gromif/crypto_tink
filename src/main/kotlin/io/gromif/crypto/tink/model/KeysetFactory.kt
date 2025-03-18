package io.gromif.crypto.tink.model

import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.Parameters

/**
 * Defines the process for [KeysetHandle] creation.
 */
interface KeysetFactory {

    suspend fun create(
        tag: String,
        keyParams: Parameters,
        associatedData: ByteArray
    ): KeysetHandle

}