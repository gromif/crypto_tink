package com.nevidimka655.crypto.tink

import com.nevidimka655.crypto.tink.extensions.sha384
import kotlin.experimental.xor

object HashStringGenerator {

    fun extendString(string: String, targetSize: Int): ByteArray {
        val bytes = string.toByteArray()
        if (string.length == targetSize) return bytes
        val sha384 = string.sha384()
        val bytesMutable = mutableListOf<Byte>()
        val originalBytesCount = bytes.size
        val shaBytesCount = sha384.size
        var origBytesIndex = 0
        var shaBytesIndex = 0
        repeat(targetSize) { index ->
            val value1 = sha384[shaBytesIndex]
            val value2 = bytes[origBytesIndex]
            val value3 = sha384[shaBytesCount - 1 - shaBytesIndex]
            val byte = ((value1 xor value2 xor value3) + index).toByte()
            bytesMutable.add(byte)
            origBytesIndex = if (origBytesIndex + 1 < originalBytesCount) origBytesIndex + 1 else 0
            shaBytesIndex = if (shaBytesIndex + 1 <= shaBytesCount) shaBytesIndex + 1 else 0
        }
        return bytesMutable.toByteArray()
    }

}