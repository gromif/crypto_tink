package io.gromif.crypto.tink.core.serializers

import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkProtoKeysetFormat
import io.gromif.crypto.tink.encoders.HexEncoder

class KeysetSerializer(
    private val hexEncoder: HexEncoder
) {

    operator fun invoke(item: KeysetHandle): String {
        return TinkProtoKeysetFormat.serializeKeyset(
            /* keysetHandle = */ item,
            /* access = */ InsecureSecretKeyAccess.get()
        ).let {
            hexEncoder.encode(bytes = it)
        }
    }

}