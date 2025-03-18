package io.gromif.crypto.tink.model

/**
 * Defines the process for reading a keyset in raw format.
 */
interface KeysetReader {

    suspend fun read(key: String): String?

}