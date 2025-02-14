package io.gromif.crypto.tink.model

interface KeysetIdUtil {

    suspend fun compute(
        tag: String,
        associatedData: ByteArray
    ): String

}