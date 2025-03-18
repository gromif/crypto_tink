package io.gromif.crypto.tink.model

/**
 * Defines the process for computing a unique keyset ID.
 */
interface KeysetIdUtil {

    suspend fun compute(
        tag: String,
        associatedData: ByteArray
    ): String

}