package com.nevidimka655.crypto.tink

import android.content.Context
import android.content.SharedPreferences
import com.google.crypto.tink.Aead
import com.google.crypto.tink.DeterministicAead
import com.google.crypto.tink.KeysetHandle
import com.nevidimka655.crypto.tink.extensions.aeadPrimitive
import com.nevidimka655.crypto.tink.extensions.deterministicAeadPrimitive
import com.nevidimka655.crypto.tink.extensions.fromBase64
import com.nevidimka655.crypto.tink.extensions.toBase64

class CryptoPreferences(
    context: Context,
    private val prefsName: String,
    aeadKeyTemplate: KeysetTemplates.AEAD,
    private var isEncryptionEnabled: Boolean
) {
    private val prefs: SharedPreferences
    val prefsEditor: SharedPreferences.Editor
    private var keysetHandleForKey: KeysetHandle
    private var keysetHandleForValue: KeysetHandle
    private val deterministicAeadPrimitiveForKey: DeterministicAead
    private var aeadPrimitiveForValue: Aead

    init {
        TinkConfig.initAead()
        TinkConfig.initDeterministic()
        prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        prefsEditor = prefs.edit()
        keysetHandleForKey = KeysetFactory.deterministic(context)
        keysetHandleForValue = KeysetFactory.aead(context, aeadKeyTemplate)
        deterministicAeadPrimitiveForKey = keysetHandleForKey.deterministicAeadPrimitive()
        aeadPrimitiveForValue = keysetHandleForValue.aeadPrimitive()
    }

    fun transform(
        context: Context,
        aead: KeysetTemplates.AEAD,
        isEncryptionEnabledNewState: Boolean = true
    ) {
        val keyList = arrayListOf<String>()
        val valueList = arrayListOf<String>()
        prefs.all.forEach {
            val key = if (isEncryptionEnabled) {
                decryptKey(it.key)
            } else it.key
            val value = if (isEncryptionEnabled) {
                decryptValue(it.value as String, key.toByteArray())
            } else it.value as String
            keyList.add(key)
            valueList.add(value)
        }
        prefsEditor.clear()
        keysetHandleForValue = KeysetFactory.aead(context, aead)
        aeadPrimitiveForValue = keysetHandleForValue.aeadPrimitive()
        isEncryptionEnabled = isEncryptionEnabledNewState
        keyList.forEachIndexed { index, s ->
            putString(s, valueList[index])
        }
        prefsEditor.commit()
    }

    fun putInt(key: String, value: Int) = apply {
        prefsEditor.putString(
            encryptKey(key),
            encryptValue(
                value.toString(),
                key.toByteArray()
            )
        )
    }

    fun putBoolean(key: String, value: Boolean) = apply {
        prefsEditor.putString(
            encryptKey(key),
            encryptValue(
                if (value) "true" else "false",
                key.toByteArray()
            )
        )
    }

    fun putString(key: String, value: String) = apply {
        prefsEditor.putString(
            encryptKey(key),
            encryptValue(value, key.toByteArray())
        )
    }

    fun getInt(key: String, defaultValue: Int): Int {
        val value = prefs.getString(
            encryptKey(key), ""
        )!!
        return if (value.isEmpty()) defaultValue
        else {
            val result = decryptValue(value, key.toByteArray()).toIntOrNull()
            result ?: defaultValue
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        val value = prefs.getString(
            encryptKey(key), ""
        )!!
        return if (value.isEmpty()) defaultValue
        else {
            decryptValue(
                value,
                key.toByteArray()
            ).toBoolean()
        }
    }

    fun getString(key: String, defaultValue: String?): String? {
        val enckey = encryptKey(key)
        val value = prefs.getString(
            enckey, null
        )
        return if (value == null) defaultValue
        else decryptValue(
            value,
            key.toByteArray()
        )
    }

    private fun encryptKey(key: String): String {
        return if (isEncryptionEnabled) deterministicAeadPrimitiveForKey.encryptDeterministically(
            key.toByteArray(), prefsName.toByteArray()
        ).toBase64()
        else key
    }

    private fun decryptKey(key: String): String {
        return deterministicAeadPrimitiveForKey.decryptDeterministically(
            key.fromBase64(), prefsName.toByteArray()
        ).decodeToString()
    }

    private fun encryptValue(
        value: String,
        associatedData: ByteArray
    ): String {
        return if (isEncryptionEnabled) aeadPrimitiveForValue.encrypt(
            value.toByteArray(), associatedData
        ).toBase64()
        else value
    }

    private fun decryptValue(
        value: String,
        associatedData: ByteArray
    ): String {
        return if (isEncryptionEnabled) {
            aeadPrimitiveForValue.decrypt(
                value.fromBase64(), associatedData
            ).decodeToString()
        } else value
    }

    fun commit() = prefsEditor.commit()

    fun apply() = prefsEditor.apply()

}