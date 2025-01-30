package com.nevidimka655.crypto.tink.core.parsers

import com.google.crypto.tink.KeysetHandle
import com.nevidimka655.crypto.tink.data.keyset.KeysetAeadFactory

class KeysetParserWithKey(
    private val keysetParserWithAead: KeysetParserWithAead,
    private val keysetAeadFactory: KeysetAeadFactory
) {

    operator fun invoke(
        serializedKeyset: String,
        key: ByteArray,
        associatedData: ByteArray
    ): KeysetHandle = keysetParserWithAead(
        serializedKeyset = serializedKeyset,
        aead = keysetAeadFactory.invoke(key = key),
        associatedData = associatedData
    )

}