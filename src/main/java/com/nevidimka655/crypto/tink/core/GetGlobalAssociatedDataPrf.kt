package com.nevidimka655.crypto.tink.core

import com.google.crypto.tink.prf.PrfSet
import com.nevidimka655.crypto.tink.data.KeysetManager
import com.nevidimka655.crypto.tink.domain.KeysetTemplates
import com.nevidimka655.crypto.tink.extensions.prf

private const val KEYSET_TAG = "9pY%MA@t"
private val KEYSET_AD = "]|d!4<I5".toByteArray()

class GetGlobalAssociatedDataPrf(
    private val keysetManager: KeysetManager
) {

    suspend operator fun invoke(): PrfSet {
        return keysetManager.getKeyset(
            tag = KEYSET_TAG,
            associatedData = KEYSET_AD,
            keyParams = KeysetTemplates.PRF.HMAC_SHA256_PRF.params
        ).prf()
    }

}