package com.nevidimka655.crypto.tink.domain.keyset

import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.Parameters

interface KeysetFactory {

    suspend fun create(
        tag: String,
        keyParams: Parameters,
        associatedData: ByteArray
    ): KeysetHandle

}