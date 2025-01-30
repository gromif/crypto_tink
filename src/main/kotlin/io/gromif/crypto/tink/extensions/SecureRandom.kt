package io.gromif.crypto.tink.extensions

import java.security.SecureRandom
import kotlin.random.asKotlinRandom

fun secureRandom(secureSeed: ByteArray) = SecureRandom()
    .apply { setSeed(secureSeed) }
    .asKotlinRandom()