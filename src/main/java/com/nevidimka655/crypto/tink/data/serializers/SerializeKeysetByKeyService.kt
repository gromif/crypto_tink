package com.nevidimka655.crypto.tink.data.serializers

import com.google.crypto.tink.KeysetHandle
import com.nevidimka655.crypto.tink.data.keyset.KeysetAeadFactory

class SerializeKeysetByKeyService(
    private val serializeKeysetByAeadService: SerializeKeysetByAeadService,
    private val keysetAeadFactory: KeysetAeadFactory
) {

    fun serialize(
        keysetHandle: KeysetHandle,
        key: ByteArray,
        associatedData: ByteArray
    ): String = serializeKeysetByAeadService.serialize(
        keysetHandle = keysetHandle,
        aead = keysetAeadFactory.invoke(key = key),
        associatedData = associatedData
    )

}