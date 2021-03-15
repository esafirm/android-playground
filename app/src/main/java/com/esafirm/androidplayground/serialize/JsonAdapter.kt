package com.esafirm.androidplayground.serialize

interface JsonAdapter {
    fun <T> from(jsonString: String): T
    fun <T : Any> to(any: T): String
}