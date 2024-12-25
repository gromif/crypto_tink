package com.nevidimka655.crypto.tink.core.serializers

import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkProtoKeysetFormat
import com.nevidimka655.crypto.tink.core.encoders.HexService

class KeysetSerializer(
    private val hexService: HexService
) {

    operator fun invoke(item: KeysetHandle): String {
        return TinkProtoKeysetFormat.serializeKeysetWithoutSecret(
            /* keysetHandle = */ item
        ).let {
            hexService.encode(bytes = it)
        }
    }

}