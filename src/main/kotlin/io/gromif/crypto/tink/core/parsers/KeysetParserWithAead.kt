package io.gromif.crypto.tink.core.parsers

import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkProtoKeysetFormat
import io.gromif.crypto.tink.core.encoders.HexUtil

class KeysetParserWithAead(
    private val hexUtil: HexUtil
) {

    operator fun invoke(
        serializedKeyset: String,
        aead: Aead,
        associatedData: ByteArray
    ): KeysetHandle {
        val encryptedKeyset = hexUtil.decode(hex = serializedKeyset)
        return TinkProtoKeysetFormat.parseEncryptedKeyset(
            /* serializedEncryptedKeyset = */ encryptedKeyset,
            /* keysetEncryptionAead = */ aead,
            /* associatedData = */ associatedData
        )
    }

}