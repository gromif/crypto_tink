package io.gromif.crypto.tink.model

interface KeysetWriter {

    suspend fun write(key: String, keyset: String)

}