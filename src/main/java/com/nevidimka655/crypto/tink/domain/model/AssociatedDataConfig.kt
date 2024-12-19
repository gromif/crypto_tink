package com.nevidimka655.crypto.tink.domain.model

import java.io.File

data class AssociatedDataConfig(
    val dataFile: File,
    val dataLength: Int,
    val dataPasswordHashLength: Int
)