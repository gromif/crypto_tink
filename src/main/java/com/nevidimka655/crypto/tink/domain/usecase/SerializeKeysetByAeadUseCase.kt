package com.nevidimka655.crypto.tink.domain.usecase

import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkProtoKeysetFormat
import com.nevidimka655.crypto.tink.domain.usecase.encoder.HexUseCase

class SerializeKeysetByAeadUseCase(
    private val hexUseCase: HexUseCase
) {

    fun serialize(
        keysetHandle: KeysetHandle,
        aead: Aead,
        associatedData: ByteArray
    ): String = TinkProtoKeysetFormat.serializeEncryptedKeyset(
        /* keysetHandle = */ keysetHandle,
        /* keysetEncryptionAead = */ aead,
        /* associatedData = */ associatedData
    ).let {
        hexUseCase.encode(bytes = it)
    }

}