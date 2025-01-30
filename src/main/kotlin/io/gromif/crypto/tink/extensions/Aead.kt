package io.gromif.crypto.tink.extensions

import com.google.crypto.tink.Aead
import io.gromif.crypto.tink.encoders.Encoder

fun Aead.encryptAndEncode(
    value: String,
    associatedData: ByteArray,
    encoder: Encoder,
): String {
    val encryptedBytes = encrypt(value.toByteArray(), associatedData)
    return encoder.encode(bytes = encryptedBytes)
}

fun Aead.decodeAndDecrypt(
    value: String,
    associatedData: ByteArray,
    encoder: Encoder,
): String {
    val encryptedBytes = encoder.decode(value = value)
    return decrypt(encryptedBytes, associatedData).decodeToString()
}