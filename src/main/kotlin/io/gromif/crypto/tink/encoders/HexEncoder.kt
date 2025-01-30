package io.gromif.crypto.tink.encoders

import com.google.crypto.tink.subtle.Hex

class HexEncoder : Encoder {

    override fun encode(bytes: ByteArray): String {
        return Hex.encode(bytes)
    }

    override fun decode(hex: String): ByteArray {
        return Hex.decode(hex)
    }

    override fun encode(value: String): String {
        return encode(value.toByteArray())
    }

    override fun decode(bytes: ByteArray): ByteArray {
        return decode(bytes.decodeToString())
    }

}