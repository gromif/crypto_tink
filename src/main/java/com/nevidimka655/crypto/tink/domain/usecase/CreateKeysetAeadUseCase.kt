package com.nevidimka655.crypto.tink.domain.usecase

import com.google.crypto.tink.subtle.AesGcmJce

class CreateKeysetAeadUseCase(
    private val createKeysetKeyUseCase: CreateKeysetKeyUseCase
) {

    fun create(key: ByteArray) = AesGcmJce(createKeysetKeyUseCase.create(key = key))

}