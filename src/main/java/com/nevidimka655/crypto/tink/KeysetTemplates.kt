package com.nevidimka655.crypto.tink

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object KeysetTemplates {

    @Serializable
    enum class AEAD {
        @SerialName("a") AES128_EAX,
        @SerialName("b") AES128_CTR_HMAC_SHA256,
        @SerialName("c") CHACHA20_POLY1305,
        @SerialName("d") AES128_GCM,
        @SerialName("e") AES256_EAX,
        @SerialName("f") AES256_CTR_HMAC_SHA256,
        @SerialName("g") XCHACHA20_POLY1305,
        @SerialName("h") AES256_GCM
    }

    @Serializable
    enum class DeterministicAEAD { @SerialName("a") AES256_SIV }

    @Serializable
    enum class Stream(val uniqueId: Int) {
        @SerialName("a") AES128_CTR_HMAC_SHA256_1MB(uniqueId = 2000),
        @SerialName("b") AES128_GCM_HKDF_1MB(uniqueId = 2001),
        @SerialName("c") AES256_CTR_HMAC_SHA256_1MB(uniqueId = 2002),
        @SerialName("d") AES256_GCM_HKDF_1MB(uniqueId = 2003),
        @SerialName("e") AES128_CTR_HMAC_SHA256_4KB(uniqueId = 2004),
        @SerialName("f") AES128_GCM_HKDF_4KB(uniqueId = 2005),
        @SerialName("g") AES256_CTR_HMAC_SHA256_4KB(uniqueId = 2006),
        @SerialName("h") AES256_GCM_HKDF_4KB(uniqueId = 2007)
    }

    // PrfKeyTemplates
    enum class PRF(val uniqueId: Int) {
        HKDF_SHA256(uniqueId = 1000)
    }

}