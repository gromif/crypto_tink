package com.nevidimka655.crypto.tink.core.serializers

import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkProtoKeysetFormat
import com.nevidimka655.crypto.tink.core.encoders.HexService

class KeysetSerializerWithAead(
    private val hexService: HexService
) {

    operator fun invoke(
        keysetHandle: KeysetHandle,
        aead: Aead,
        associatedData: ByteArray
    ): String = TinkProtoKeysetFormat.serializeEncryptedKeyset(
        /* keysetHandle = */ keysetHandle,
        /* keysetEncryptionAead = */ aead,
        /* associatedData = */ associatedData
    ).let {
        hexService.encode(bytes = it)
    }

}