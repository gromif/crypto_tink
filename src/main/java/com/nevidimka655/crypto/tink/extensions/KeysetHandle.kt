package com.nevidimka655.crypto.tink.extensions

import com.google.crypto.tink.Aead
import com.google.crypto.tink.DeterministicAead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.Mac
import com.google.crypto.tink.StreamingAead
import com.google.crypto.tink.prf.PrfSet

fun KeysetHandle.streamingAeadPrimitive(): StreamingAead =
    this.getPrimitive(StreamingAead::class.java)

fun KeysetHandle.aeadPrimitive(): Aead = getPrimitive(Aead::class.java)

fun KeysetHandle.deterministicAeadPrimitive(): DeterministicAead =
    getPrimitive(DeterministicAead::class.java)

fun KeysetHandle.macPrimitive(): Mac = getPrimitive(Mac::class.java)

fun KeysetHandle.prfPrimitive(): PrfSet = getPrimitive(PrfSet::class.java)