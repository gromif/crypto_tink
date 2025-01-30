package io.gromif.crypto.tink.core.parsers

import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkProtoKeysetFormat
import io.gromif.crypto.tink.encoders.HexEncoder

class KeysetParserWithAead(
    private val hexEncoder: HexEncoder
) {

    operator fun invoke(
        serializedKeyset: String,
        aead: Aead,
        associatedData: ByteArray
    ): KeysetHandle {
        val encryptedKeyset = hexEncoder.decode(hex = serializedKeyset)
        return TinkProtoKeysetFormat.parseEncryptedKeyset(
            /* serializedEncryptedKeyset = */ encryptedKeyset,
            /* keysetEncryptionAead = */ aead,
            /* associatedData = */ associatedData
        )
    }

}