package com.nevidimka655.crypto.tink

enum class KeysetGroupId(val char: Char) {

    AEAD_DEFAULT('w'),
    AEAD_NOTES('s'),

    STREAM_DEFAULT('e'),

    DETERMINISTIC('q');

}