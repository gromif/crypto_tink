package com.nevidimka655.crypto.tink.domain.usecase.encoder

import com.google.crypto.tink.subtle.Hex

class HexUseCase {

    fun encode(bytes: ByteArray): String {
        return Hex.encode(bytes)
    }

    fun decode(hex: String): ByteArray {
        return Hex.decode(hex)
    }

}