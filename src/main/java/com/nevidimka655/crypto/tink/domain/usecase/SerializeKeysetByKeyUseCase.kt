package com.nevidimka655.crypto.tink.domain.usecase

import com.google.crypto.tink.KeysetHandle

class SerializeKeysetByKeyUseCase(
    private val serializeKeysetByAeadUseCase: SerializeKeysetByAeadUseCase,
    private val createKeysetAeadUseCase: CreateKeysetAeadUseCase
) {

    fun serialize(
        keysetHandle: KeysetHandle,
        key: ByteArray,
        associatedData: ByteArray
    ): String = serializeKeysetByAeadUseCase.serialize(
        keysetHandle = keysetHandle,
        aead = createKeysetAeadUseCase.create(key = key),
        associatedData = associatedData
    )

}