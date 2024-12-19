package com.nevidimka655.crypto.tink.domain.model.keyset

interface PrefsKeyName {

    fun get(
        tag: String,
        associatedData: ByteArray
    ): String

}