package io.gromif.crypto.tink.core.serializers

import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkProtoKeysetFormat
import io.gromif.crypto.tink.encoders.HexUtil

class KeysetSerializer(
    private val hexUtil: HexUtil
) {

    operator fun invoke(item: KeysetHandle): String {
        return TinkProtoKeysetFormat.serializeKeyset(
            /* keysetHandle = */ item,
            /* access = */ InsecureSecretKeyAccess.get()
        ).let {
            hexUtil.encode(bytes = it)
        }
    }

}