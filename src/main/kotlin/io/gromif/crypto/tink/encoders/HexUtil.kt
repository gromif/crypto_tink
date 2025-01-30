package io.gromif.crypto.tink.encoders

import com.google.crypto.tink.subtle.Hex

class HexUtil {

    fun encode(bytes: ByteArray): String {
        return Hex.encode(bytes)
    }

    fun decode(hex: String): ByteArray {
        return Hex.decode(hex)
    }

}