package com.nevidimka655.crypto.tink.data.parsers

import com.google.crypto.tink.KeysetHandle
import com.nevidimka655.crypto.tink.data.keyset.KeysetAeadFactory

class ParseKeysetByKeyService(
    private val parseKeysetByAeadService: ParseKeysetByAeadService,
    private val keysetAeadFactory: KeysetAeadFactory
) {

    fun parse(
        serializedKeyset: String,
        key: ByteArray,
        associatedData: ByteArray
    ): KeysetHandle = parseKeysetByAeadService.parse(
        serializedKeyset = serializedKeyset,
        aead = keysetAeadFactory.invoke(key = key),
        associatedData = associatedData
    )

}