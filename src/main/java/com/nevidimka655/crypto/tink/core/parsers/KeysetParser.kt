package com.nevidimka655.crypto.tink.core.parsers

import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkProtoKeysetFormat
import com.nevidimka655.crypto.tink.core.encoders.HexService

class KeysetParser(
    private val hexService: HexService
) {

    operator fun invoke(
        serializedKeyset: String
    ): KeysetHandle {
        val encryptedKeyset = hexService.decode(hex = serializedKeyset)
        return TinkProtoKeysetFormat.parseKeysetWithoutSecret(
            /* serializedKeyset = */ encryptedKeyset
        )
    }

}