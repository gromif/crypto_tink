package io.gromif.crypto.tink.domain.keyset

interface MasterKeyName {

    suspend fun get(
        tag: String,
        associatedData: ByteArray
    ): String

}