package com.nevidimka655.crypto.tink.domain.usecase.hash

import java.security.MessageDigest

class Sha256UseCase {

    fun compute(value: ByteArray): ByteArray = MessageDigest.getInstance("SHA-256")
        .digest(value)

}