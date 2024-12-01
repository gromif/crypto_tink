package com.nevidimka655.crypto.tink

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.collection.SparseArrayCompat
import androidx.core.content.edit
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.integration.android.AndroidKeystoreKmsClient
import com.google.crypto.tink.subtle.AesGcmJce
import com.google.crypto.tink.subtle.Hex
import com.nevidimka655.crypto.tink.extensions.aeadPrimitive
import com.nevidimka655.crypto.tink.extensions.sha384
import java.io.File
import java.security.SecureRandom
import kotlin.random.Random

class KeysetFactory(
    private val associatedDataConfig: AssociatedDataConfig,
    private val prefsConfig: PrefsConfig
) {
    data class AssociatedDataConfig(
        val dataFile: File,
        val dataLength: Int,
        val dataPasswordHashLength: Int
    )

    data class PrefsConfig(
        val prefsFileNameDefault: String,
        val prefs: SharedPreferences,
        val prefsUniqueSaltFieldKey: String
    )

    private val keysetList = SparseArrayCompat<KeysetHandle>()
    private val dataFile get() = associatedDataConfig.dataFile

    private var uniqueSaltVar = 0
    val uniqueSalt get() = if (uniqueSaltVar == 0) loadUniqueUserSalt() else uniqueSaltVar

    private var decodedAssociatedData: ByteArray? = null
    val associatedData
        get() = decodedAssociatedData ?: initAssociatedData(
            ByteArray(associatedDataConfig.dataLength)
        ).also { decodedAssociatedData = it }

    fun deterministic(context: Context) = getKeyset(
        context = context,
        aeadName = KeysetTemplates.DeterministicAEAD.AES256_SIV.name,
        aeadOrdinal = KeysetTemplates.DeterministicAEAD.AES256_SIV.ordinal,
        aliasEncoderLetter = KeysetGroupId.DETERMINISTIC.char
    )

    fun aead(
        context: Context,
        aead: KeysetTemplates.AEAD,
        keysetGroupId: KeysetGroupId = KeysetGroupId.AEAD_DEFAULT
    ) = getKeyset(
        context = context,
        aeadName = aead.name,
        aeadOrdinal = aead.ordinal,
        aliasEncoderLetter = keysetGroupId.char
    )

    fun stream(
        context: Context,
        aead: KeysetTemplates.Stream,
        keysetGroupId: KeysetGroupId = KeysetGroupId.STREAM_DEFAULT
    ): KeysetHandle {
        val alias = aead.uniqueId
        return keysetList.get(alias) ?: buildKeysetManager(
            context = context,
            keyTemplateSign = aead.name,
            masterKeyAlias = getMasterKeyAliasForKeyset(
                aead.name,
                keysetGroupId.char,
                aead.ordinal
            ),
            prefsAlias = getPreferencesAliasForKeyset(aead.name, keysetGroupId.char, aead.ordinal)
        ).keysetHandle.also { keysetList.append(alias, it) }
    }

    fun pseudoRandomFunction(context: Context, template: KeysetTemplates.PRF): KeysetHandle {
        val alias = template.uniqueId
        return keysetList.get(alias) ?: buildKeysetManager(
            context = context,
            keyTemplateSign = template.name,
            masterKeyAlias = Hex.encode("$uniqueSalt${template.uniqueId}".sha384()),
            prefsAlias = Hex.encode("${template.name}${template.uniqueId}$uniqueSalt".sha384())
        ).keysetHandle.also { keysetList.append(alias, it) }
    }

    private fun getKeyset(
        context: Context,
        aeadName: String,
        aeadOrdinal: Int,
        aliasEncoderLetter: Char
    ): KeysetHandle {
        val alias = getPreferencesAliasForKeyset(aeadName, aliasEncoderLetter, aeadOrdinal)
        val aliasHash = alias.hashCode()
        return keysetList.get(aliasHash) ?: buildKeysetManager(
            context = context,
            keyTemplateSign = aeadName,
            masterKeyAlias = getMasterKeyAliasForKeyset(aeadName, aliasEncoderLetter, aeadOrdinal),
            prefsAlias = alias
        ).keysetHandle.also { keysetList.append(aliasHash, it) }
    }

    private fun getPreferencesAliasForKeyset(
        aeadName: String,
        prefix: Char,
        ordinal: Int
    ): String {
        val pseudoRandom = aeadName.hashCode() xor prefix.code - ordinal + uniqueSalt
        val bytes = "$pseudoRandom".toByteArray()
        return Hex.encode(bytes)
    }

    private fun getMasterKeyAliasForKeyset(
        aeadName: String,
        prefix: Char,
        ordinal: Int
    ): String {
        val pseudoRandom = aeadName.hashCode() / prefix.code - ordinal - uniqueSalt
        val bytes = "$pseudoRandom".toByteArray()
        return Hex.encode(bytes)
    }

    private fun buildKeysetManager(
        context: Context,
        keyTemplateSign: String,
        masterKeyAlias: String,
        prefsFileName: String = prefsConfig.prefsFileNameDefault,
        prefsAlias: String
    ): AndroidKeysetManager = AndroidKeysetManager.Builder()
        .withKeyTemplate(KeyTemplates.get(keyTemplateSign))
        .withMasterKeyUri("${AndroidKeystoreKmsClient.PREFIX}$masterKeyAlias")
        .withSharedPref(context, prefsAlias, prefsFileName)
        .build()

    fun initEncryptedAssociatedData(rawPassword: String) {
        if (dataFile.exists()) {
            val password = HashStringGenerator.extendString(
                rawPassword, associatedDataConfig.dataPasswordHashLength
            )
            val aead = AesGcmJce(password)
            dataFile.inputStream().use {
                val decodedBytes =
                    aead.decrypt(it.readBytes(), dataFile.name.toByteArray())
                if (decodedBytes.size == associatedDataConfig.dataLength) {
                    decodedAssociatedData = decodedBytes
                }
            }
        }
    }

    fun encryptAssociatedData(rawPassword: String) {
        val password = HashStringGenerator.extendString(
            rawPassword, associatedDataConfig.dataPasswordHashLength
        )
        val aead = AesGcmJce(password)
        val byteArray = associatedData
        dataFile.outputStream().use {
            it.write(
                aead.encrypt(byteArray, dataFile.name.toByteArray())
            )
        }
    }

    fun decryptAssociatedData() {
        dataFile.run {
            delete()
            createNewFile()
            outputStream()
        }.use {
            it.write(associatedData)
        }
    }


    private fun initAssociatedData(byteArray: ByteArray): ByteArray {
        if (dataFile.exists()) {
            dataFile.inputStream().use {
                it.read(byteArray)
            }
        } else {
            generateAssociatedData(byteArray)
            dataFile.createNewFile()
            dataFile.outputStream().use {
                it.write(byteArray)
            }
        }
        return byteArray
    }

    fun generateAssociatedData(byteArray: ByteArray) = run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) SecureRandom.getInstanceStrong()
        else SecureRandom()
    }.nextBytes(byteArray)

    fun setAssociatedDataExplicitly(data: ByteArray) {
        decodedAssociatedData = data
    }

    fun transformAssociatedDataToWorkInstance(
        context: Context,
        bytesIn: ByteArray,
        encryptionMode: Boolean,
        authenticationTag: String
    ): ByteArray {
        val aead = aead(context, KeysetTemplates.AEAD.AES256_GCM).aeadPrimitive()
        return with(aead) {
            val tag = authenticationTag.sha384()
            if (encryptionMode) encrypt(bytesIn, tag)
            else decrypt(bytesIn, tag).also { decodedAssociatedData = it }
        }
    }

    private fun loadUniqueUserSalt(): Int {
        var salt = prefsConfig.prefs.getInt(
            prefsConfig.prefsUniqueSaltFieldKey,
            0
        )
        while (salt == 0) {
            salt = Random.nextInt()
            prefsConfig.prefs.edit(true) {
                putInt(prefsConfig.prefsUniqueSaltFieldKey, salt)
            }
        }
        return salt
    }

    fun saveKeystoreFile() = prefsConfig.prefs.edit().commit()

}