package com.nevidimka655.crypto.tink.data.keyset

import com.google.crypto.tink.subtle.AesGcmJce

class KeysetAeadFactory(
    private val keysetKeyFactory: KeysetKeyFactory
) {

    fun invoke(key: ByteArray) = AesGcmJce(keysetKeyFactory.invoke(key = key))

}