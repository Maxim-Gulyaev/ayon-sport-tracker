package com.maxim.common.util

import android.util.Log

private const val TAG = "ayon_log"
private const val ERROR_TAG = "ayon_error"

interface AyonLogger {
    fun e(message: String = "Error!", throwable: Throwable? = null, tag: String = ERROR_TAG)
    fun d(message: String = "This works!", tag: String = TAG)
}

class AyonLog: AyonLogger {

    override fun e(message: String, throwable: Throwable?, tag: String) {
        Log.e(tag, message, throwable)
    }

    override fun d(message: String, tag: String) {
        Log.d(tag, message)
    }
}

object NoopLog: AyonLogger {

    override fun e(message: String, throwable: Throwable?, tag: String) = Unit

    override fun d(message: String, tag: String) = Unit
}