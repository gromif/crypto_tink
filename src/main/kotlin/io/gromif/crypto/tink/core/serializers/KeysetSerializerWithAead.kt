package io.gromif.crypto.tink.core.serializers

import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkProtoKeysetFormat
import io.gromif.crypto.tink.encoders.HexUtil

class KeysetSerializerWithAead(
    private val hexUtil: HexUtil
) {

    operator fun invoke(
        keysetHandle: KeysetHandle,
        aead: Aead,
        associatedData: ByteArray
    ): String = TinkProtoKeysetFormat.serializeEncryptedKeyset(
        /* keysetHandle = */ keysetHandle,
        /* keysetEncryptionAead = */ aead,
        /* associatedData = */ associatedData
    ).let {
        hexUtil.encode(bytes = it)
    }

}