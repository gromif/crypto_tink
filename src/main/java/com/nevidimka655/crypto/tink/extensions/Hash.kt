package com.nevidimka655.crypto.tink.extensions

import java.security.MessageDigest

fun String.sha384(): ByteArray = MessageDigest.getInstance("SHA-384").digest(toByteArray())