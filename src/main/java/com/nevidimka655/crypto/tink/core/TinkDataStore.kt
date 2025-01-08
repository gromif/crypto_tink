package com.nevidimka655.crypto.tink.core

import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.prf.PrfSet
import com.nevidimka655.crypto.tink.core.encoders.Base64Util
import com.nevidimka655.crypto.tink.domain.KeysetTemplates
import com.nevidimka655.crypto.tink.extensions.aead
import com.nevidimka655.crypto.tink.extensions.prf

abstract class TinkDataStore(private val base64Util: Base64Util) {
    private var keyPrf: PrfSet? = null
    private var valueAead: Aead? = null
    protected val defaultAeadTemplateIndex = KeysetTemplates.AEAD.AES128_EAX.ordinal
    protected open val keyPrfHashSize = 24

    protected abstract suspend fun getAeadTemplate(): KeysetTemplates.AEAD?

    protected abstract suspend fun createKeyPrfKeyset(): KeysetHandle?

    protected abstract suspend fun createValueAeadKeyset(): KeysetHandle?

    protected suspend fun hashKey(key: String) = getKeyPrf()?.let {
        val prfBytes = it.computePrimary(key.toByteArray(), keyPrfHashSize)
        base64Util.encode(bytes = prfBytes)
    } ?: key

    protected suspend fun encrypt(key: String, value: String) = getValueAead()?.let {
        val encryptedBytes = it.encrypt(value.toByteArray(), key.toByteArray())
        base64Util.encode(bytes = encryptedBytes)
    } ?: value

    protected suspend fun decrypt(key: String, value: String) = getValueAead()?.let {
        val encryptedBytes = base64Util.decode(value = value)
        it.decrypt(encryptedBytes, key.toByteArray()).decodeToString()
    } ?: value

    private suspend fun getKeyPrf(): PrfSet? {
        return keyPrf ?: createKeyPrfKeyset()?.prf()
            .also { keyPrf = it }
    }

    private suspend fun getValueAead(): Aead? {
        return valueAead ?: createValueAeadKeyset()?.aead()
            .also { valueAead = it }
    }

}