package com.nevidimka655.crypto.tink.core.encoders

import com.google.crypto.tink.subtle.Base64

class Base64Service {

    fun encode(bytes: ByteArray): String = Base64.encode(bytes)
    fun decode(value: String): ByteArray = Base64.decode(value)

    fun encode(value: String): String = encode(bytes = value.toByteArray())
    fun decode(bytes: ByteArray): ByteArray = decode(value = bytes.decodeToString())

}