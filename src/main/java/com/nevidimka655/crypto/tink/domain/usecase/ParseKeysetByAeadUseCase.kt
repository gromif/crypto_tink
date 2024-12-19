package com.nevidimka655.crypto.tink.domain.usecase

import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkProtoKeysetFormat
import com.nevidimka655.crypto.tink.domain.usecase.encoder.HexUseCase

class ParseKeysetByAeadUseCase(
    private val hexUseCase: HexUseCase
) {

    fun parse(
        serializedKeyset: String,
        aead: Aead,
        associatedData: ByteArray
    ): KeysetHandle {
        val encryptedKeyset = hexUseCase.decode(hex = serializedKeyset)
        return TinkProtoKeysetFormat.parseEncryptedKeyset(
            /* serializedEncryptedKeyset = */ encryptedKeyset,
            /* keysetEncryptionAead = */ aead,
            /* associatedData = */ associatedData
        )
    }

}