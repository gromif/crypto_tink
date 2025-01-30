package io.gromif.crypto.tink.data.keyset

import io.gromif.crypto.tink.core.hash.Sha384Util

class KeysetKeyFactory(
    private val sha384Util: Sha384Util
) {

    fun invoke(key: ByteArray) = sha384Util.compute(value = key).copyOf(32)

}