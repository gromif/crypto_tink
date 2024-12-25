package com.nevidimka655.crypto.tink.core.parsers

import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkProtoKeysetFormat
import com.nevidimka655.crypto.tink.core.encoders.HexUtil

class KeysetParser(
    private val hexUtil: HexUtil
) {

    operator fun invoke(
        serializedKeyset: String
    ): KeysetHandle {
        val encryptedKeyset = hexUtil.decode(hex = serializedKeyset)
        return TinkProtoKeysetFormat.parseKeysetWithoutSecret(
            /* serializedKeyset = */ encryptedKeyset
        )
    }

}