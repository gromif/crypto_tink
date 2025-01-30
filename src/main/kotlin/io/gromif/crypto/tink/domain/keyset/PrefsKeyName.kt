package io.gromif.crypto.tink.domain.keyset

interface PrefsKeyName {

    fun get(
        tag: String,
        associatedData: ByteArray
    ): String

}