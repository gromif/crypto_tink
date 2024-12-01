package com.nevidimka655.crypto.tink.extensions

import java.security.MessageDigest

fun String.sha384(): ByteArray = MessageDigest.getInstance("SHA-384").digest(toByteArray())
fun String.sha256(): ByteArray = MessageDigest.getInstance("SHA-256").digest(toByteArray())