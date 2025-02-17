package io.gromif.crypto.tink.extensions

import io.gromif.crypto.tink.model.AeadTemplate
import io.gromif.crypto.tink.model.KeysetTemplates
import io.gromif.crypto.tink.model.KeysetTemplates.AEAD.entries

fun KeysetTemplates.AEAD.Companion.getTemplateList(): List<AeadTemplate> = entries.map {
    AeadTemplate(id = it.ordinal, name = it.name.lowercase())
}

private fun KeysetTemplates.Stream.Companion.getTemplateList(suffix: String): List<AeadTemplate> {
    return entries.filter { it.name.endsWith(suffix) }.map {
        AeadTemplate(id = it.ordinal, name = it.name.removeSuffix(suffix).lowercase())
    }
}

fun KeysetTemplates.Stream.Companion.getTemplateList1MB(): List<AeadTemplate> = getTemplateList("_1MB")

fun KeysetTemplates.Stream.Companion.getTemplateList4KB(): List<AeadTemplate> = getTemplateList("_4KB")