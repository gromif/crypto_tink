package io.gromif.crypto.tink.core.parsers

import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkProtoKeysetFormat
import io.gromif.crypto.tink.encoders.HexEncoder

class KeysetParser(
    private val hexEncoder: HexEncoder
) {

    operator fun invoke(
        serializedKeyset: String
    ): KeysetHandle {
        val encryptedKeyset = hexEncoder.decode(hex = serializedKeyset)
        return TinkProtoKeysetFormat.parseKeyset(
            /* serializedKeyset = */ encryptedKeyset,
            /* access = */ InsecureSecretKeyAccess.get()
        )
    }

}