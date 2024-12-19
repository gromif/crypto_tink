package com.nevidimka655.crypto.tink.data.keyset

import com.nevidimka655.crypto.tink.core.hash.Sha384Service

class KeysetKeyFactory(
    private val sha384Service: Sha384Service
) {

    fun invoke(key: ByteArray) = sha384Service.compute(value = key).copyOf(32)

}