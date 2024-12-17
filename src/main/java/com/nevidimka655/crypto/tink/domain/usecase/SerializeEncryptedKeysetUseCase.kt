package com.nevidimka655.crypto.tink.domain.usecase

import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkProtoKeysetFormat

class SerializeEncryptedKeysetUseCase(
    private val createKeysetAeadUseCase: CreateKeysetAeadUseCase
) {

    fun serialize(
        keysetHandle: KeysetHandle,
        key: ByteArray,
        associatedData: ByteArray
    ): String = TinkProtoKeysetFormat.serializeEncryptedKeyset(
        /*keysetHandle*/ keysetHandle,
        /*keysetEncryptionAead*/ createKeysetAeadUseCase.create(key = key),
        /*associatedData*/ associatedData
    ).decodeToString()

}