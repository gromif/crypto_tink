package com.nevidimka655.crypto.tink.extensions

import com.google.crypto.tink.Aead
import com.google.crypto.tink.DeterministicAead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.Mac
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.StreamingAead
import com.google.crypto.tink.prf.PrfSet

fun KeysetHandle.streamingAeadPrimitive(): StreamingAead =
    this.getPrimitive(RegistryConfiguration.get(), StreamingAead::class.java)

fun KeysetHandle.aeadPrimitive(): Aead =
    this.getPrimitive(RegistryConfiguration.get(), Aead::class.java)

fun KeysetHandle.deterministicAeadPrimitive(): DeterministicAead = this.getPrimitive(
    RegistryConfiguration.get(),
    DeterministicAead::class.java
)

fun KeysetHandle.macPrimitive(): Mac =
    this.getPrimitive(RegistryConfiguration.get(), Mac::class.java)

fun KeysetHandle.prf(): PrfSet = getPrimitive(RegistryConfiguration.get(), PrfSet::class.java)