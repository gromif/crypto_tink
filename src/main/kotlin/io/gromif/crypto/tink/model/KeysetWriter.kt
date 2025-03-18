package io.gromif.crypto.tink.model

/**
 * Defines the process for writing a keyset in raw format.
 */
interface KeysetWriter {

    suspend fun write(key: String, keyset: String)

}