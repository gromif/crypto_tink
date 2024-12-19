package com.nevidimka655.crypto.tink.extensions

import com.google.crypto.tink.Aead
import com.google.crypto.tink.DeterministicAead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.Mac
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.StreamingAead
import com.google.crypto.tink.prf.PrfSet
import kotlin.jvm.java

fun KeysetHandle.streamingAeadPrimitive(): StreamingAead = primitive()
fun KeysetHandle.aeadPrimitive(): Aead = primitive()
fun KeysetHandle.deterministicAeadPrimitive(): DeterministicAead = primitive()
fun KeysetHandle.macPrimitive(): Mac = primitive()
fun KeysetHandle.prfPrimitive(): PrfSet = primitive()

private inline fun <reified T> KeysetHandle.primitive(): T =
    getPrimitive(RegistryConfiguration.get(), T::class.javaClass) as T