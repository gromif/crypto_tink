package io.gromif.crypto.tink.core.parsers

import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkProtoKeysetFormat
import io.gromif.crypto.tink.encoders.HexUtil

class KeysetParser(
    private val hexUtil: HexUtil
) {

    operator fun invoke(
        serializedKeyset: String
    ): KeysetHandle {
        val encryptedKeyset = hexUtil.decode(hex = serializedKeyset)
        return TinkProtoKeysetFormat.parseKeyset(
            /* serializedKeyset = */ encryptedKeyset,
            /* access = */ InsecureSecretKeyAccess.get()
        )
    }

}