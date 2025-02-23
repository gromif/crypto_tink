package io.gromif.crypto.tink.extensions

import io.gromif.crypto.tink.model.AeadTemplate
import io.gromif.crypto.tink.model.KeysetTemplates

fun KeysetTemplates.AEAD.Companion.getTemplateList(): List<AeadTemplate> {
    return KeysetTemplates.AEAD.entries.map {
        AeadTemplate(id = it.ordinal, name = it.name.lowercase())
    }
}

private fun KeysetTemplates.Stream.Companion.getTemplateList(suffix: String): List<AeadTemplate> {
    return KeysetTemplates.Stream.entries.filter { it.name.endsWith(suffix) }.map {
        AeadTemplate(id = it.ordinal, name = it.name.removeSuffix(suffix).lowercase())
    }
}

fun KeysetTemplates.Stream.Companion.getTemplateList1MB(): List<AeadTemplate> = getTemplateList("_1MB")

fun KeysetTemplates.Stream.Companion.getTemplateList4KB(): List<AeadTemplate> = getTemplateList("_4KB")