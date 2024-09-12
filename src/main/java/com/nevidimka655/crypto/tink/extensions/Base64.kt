package com.nevidimka655.crypto.tink.extensions

import com.google.crypto.tink.subtle.Base64

fun ByteArray.toBase64(): String = Base64.encode(this)
fun String.toBase64(): String = Base64.encode(toByteArray())
fun ByteArray.fromBase64(): ByteArray = Base64.decode(decodeToString())
fun String.fromBase64(): ByteArray = Base64.decode(this)