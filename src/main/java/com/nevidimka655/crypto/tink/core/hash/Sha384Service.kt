package com.nevidimka655.crypto.tink.core.hash

import java.security.MessageDigest

class Sha384Service {

    fun compute(value: ByteArray): ByteArray = MessageDigest.getInstance("SHA-384")
        .digest(value)

}