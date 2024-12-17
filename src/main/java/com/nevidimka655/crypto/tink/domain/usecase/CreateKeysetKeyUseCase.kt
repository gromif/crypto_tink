package com.nevidimka655.crypto.tink.domain.usecase

import com.nevidimka655.crypto.tink.domain.usecase.hash.Sha384UseCase

class CreateKeysetKeyUseCase(
    private val sha384UseCase: Sha384UseCase
) {

    fun create(key: ByteArray) = sha384UseCase.compute(value = key).copyOf(32)

}