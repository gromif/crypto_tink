package io.gromif.crypto.tink.model

interface KeysetReader {

    suspend fun read(key: String): String?

}