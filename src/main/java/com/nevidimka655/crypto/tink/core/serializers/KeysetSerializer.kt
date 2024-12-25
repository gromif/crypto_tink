package com.nevidimka655.crypto.tink.core.serializers

import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkProtoKeysetFormat
import com.nevidimka655.crypto.tink.core.encoders.HexUtil

class KeysetSerializer(
    private val hexUtil: HexUtil
) {

    operator fun invoke(item: KeysetHandle): String {
        return TinkProtoKeysetFormat.serializeKeysetWithoutSecret(
            /* keysetHandle = */ item
        ).let {
            hexUtil.encode(bytes = it)
        }
    }

}