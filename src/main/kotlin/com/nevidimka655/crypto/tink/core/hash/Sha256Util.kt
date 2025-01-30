package com.nevidimka655.crypto.tink.core.hash

import java.security.MessageDigest

class Sha256Util {

    fun compute(value: ByteArray): ByteArray = MessageDigest.getInstance("SHA-256")
        .digest(value)

}