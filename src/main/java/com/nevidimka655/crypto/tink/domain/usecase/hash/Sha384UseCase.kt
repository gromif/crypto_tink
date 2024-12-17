package com.nevidimka655.crypto.tink.domain.usecase.hash

import java.security.MessageDigest

class Sha384UseCase {

    fun compute(value: ByteArray): ByteArray = MessageDigest.getInstance("SHA-384")
        .digest(value)

}