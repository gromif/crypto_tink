package com.nevidimka655.crypto.tink.data

import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.daead.DeterministicAeadConfig
import com.google.crypto.tink.mac.MacConfig
import com.google.crypto.tink.prf.PrfConfig
import com.google.crypto.tink.streamingaead.StreamingAeadConfig

object TinkConfig {

    fun init() {
        initStream()
        initAead()
        initPrf()
        //initMac()
    }

    /**
     * Initializes Tink's config for Aead
     */
    fun initAead() = AeadConfig.register()

    /**
     * Initializes Tink's config for DeterministicAead
     */
    fun initDeterministic() = DeterministicAeadConfig.register()

    /**
     * Initializes Tink's config for Mac
     */
    fun initMac() = MacConfig.register()

    /**
     * Initializes Tink's config for StreamingAead
     */
    fun initStream() = StreamingAeadConfig.register()

    /**
     * Initializes Tink's config for PRF
     */
    fun initPrf() = PrfConfig.register()

}