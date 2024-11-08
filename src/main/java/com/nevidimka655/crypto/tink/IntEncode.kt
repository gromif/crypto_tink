package com.nevidimka655.crypto.tink

import java.math.BigInteger

object IntEncode {

    fun encode(
        key: Int,
        associatedInt: Int,
        integer: Int
    ): Int {
        var result = integer + associatedInt
        BigInteger.valueOf((key - associatedInt).toLong()).toByteArray()
            .forEachIndexed { index, byte ->
                val operation = index % 2 == 0
                val value = byte.toInt()
                val valueForOperation = value * (index - value)
                result = if (operation) result - valueForOperation
                else result + valueForOperation
            }
        return result
    }

    fun decode(
        key: Int,
        associatedInt: Int,
        integer: Int
    ): Int {
        var result = integer
        BigInteger.valueOf((key - associatedInt).toLong()).toByteArray()
            .forEachIndexed { index, byte ->
                val operation = index % 2 == 0
                val value = byte.toInt()
                val valueForOperation = value * (index - value)
                result = if (operation) result + valueForOperation
                else result - valueForOperation
            }
        return result - associatedInt
    }

}