package com.nevidimka655.crypto.tink

object KeysetTemplates {
    enum class AEAD {
        AES128_EAX, AES128_CTR_HMAC_SHA256,
        CHACHA20_POLY1305, AES128_GCM,
        AES256_EAX, AES256_CTR_HMAC_SHA256,
        XCHACHA20_POLY1305, AES256_GCM
    }

    enum class DeterministicAEAD { AES256_SIV }
    enum class Stream(val uniqueId: Int) {
        AES128_CTR_HMAC_SHA256_1MB(uniqueId = 2000), AES128_GCM_HKDF_1MB(uniqueId = 2001),
        AES256_CTR_HMAC_SHA256_1MB(uniqueId = 2002), AES256_GCM_HKDF_1MB(uniqueId = 2003),
        AES128_CTR_HMAC_SHA256_4KB(uniqueId = 2004), AES128_GCM_HKDF_4KB(uniqueId = 2005),
        AES256_CTR_HMAC_SHA256_4KB(uniqueId = 2006), AES256_GCM_HKDF_4KB(uniqueId = 2007)
    }

    // PrfKeyTemplates
    enum class PRF(val uniqueId: Int) {
        HKDF_SHA256(uniqueId = 1000)
    }

}