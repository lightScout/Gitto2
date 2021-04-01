
@file:Suppress("methodName")
package com.britishbroadcast.gitto.util

import android.util.Log

fun debugLogger(errorMessage: String?) {
    Log.d("TAG_M", errorMessage.toString())
}