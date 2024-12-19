package com.nevidimka655.crypto.tink.core.encoders

import com.google.crypto.tink.subtle.Hex

class HexService {

    fun encode(bytes: ByteArray): String {
        return Hex.encode(bytes)
    }

    fun decode(hex: String): ByteArray {
        return Hex.decode(hex)
    }

}