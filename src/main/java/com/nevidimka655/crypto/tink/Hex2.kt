package com.nevidimka655.crypto.tink

import com.google.crypto.tink.subtle.Hex

object Hex2 {

    object Modes {
        const val A = 0
        const val B = 1
    }

    fun encode(mode: Int, bytes: ByteArray) = Hex.encode(bytes).toByteArray().run {
        if (mode == Modes.A) map { encodeByteA(it) }
        else map { encodeByteB(it) }
    }.toByteArray().decodeToString()

    fun decode(mode: Int, string: String) = string.toByteArray().run {
        val decodedHexBytes = if (mode == Modes.A) map { decodeByteA(it) }
        else map { decodeByteB(it) }
        Hex.decode(decodedHexBytes.toByteArray().decodeToString())
    }

    private fun encodeByteA(byte: Byte) = when(byte.toInt()) {
        48 -> '>'.code
        49 -> '*'.code
        50 -> '$'.code
        51 -> '~'.code
        52 -> '#'.code
        53 -> '-'.code
        54 -> '%'.code
        55 -> '@'.code
        56 -> '!'.code
        57 -> '_'.code
        97 -> ','.code
        98 -> ']'.code
        99 -> '+'.code
        100 -> '['.code
        101 -> '?'.code
        102 -> '.'.code
        else -> 0
    }.toByte()

    private fun decodeByteA(byte: Byte) = when(byte.toInt()) {
        '>'.code -> 48
        '*'.code -> 49
        '$'.code -> 50
        '~'.code -> 51
        '#'.code -> 52
        '-'.code -> 53
        '%'.code -> 54
        '@'.code -> 55
        '!'.code -> 56
        '_'.code -> 57
        ','.code -> 97
        ']'.code -> 98
        '+'.code -> 99
         '['.code -> 100
         '?'.code -> 101
         '.'.code -> 102
        else -> 0
    }.toByte()

    private fun encodeByteB(byte: Byte) = when(byte.toInt()) {
        48 -> 'G'.code
        49 -> 'H'.code
        50 -> 'A'.code
        51 -> 'T'.code
        52 -> 'C'.code
        53 -> 'Q'.code
        54 -> 'U'.code
        55 -> 'I'.code
        56 -> 'N'.code
        57 -> 'Z'.code
        97 -> 'X'.code
        98 -> 'r'.code
        99 -> 'e'.code
        100 -> 'y'.code
        101 -> 'l'.code
        102 -> 'S'.code
        else -> 0
    }.toByte()

    private fun decodeByteB(byte: Byte) = when(byte.toInt()) {
        'G'.code -> 48
        'H'.code -> 49
        'A'.code -> 50
        'T'.code -> 51
        'C'.code -> 52
        'Q'.code -> 53
        'U'.code -> 54
        'I'.code -> 55
        'N'.code -> 56
        'Z'.code -> 57
        'X'.code -> 97
        'r'.code -> 98
        'e'.code -> 99
        'y'.code -> 100
        'l'.code -> 101
        'S'.code -> 102
        else -> 0
    }.toByte()

}