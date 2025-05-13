package com.sujoy.pbc.presentation.Util

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns

fun getFileSizeInMB(context: Context, uri: Uri): Double {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    var size: Long = 0
    cursor?.use {
        val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
        if (it.moveToFirst()) {
            size = it.getLong(sizeIndex)
        }
    }
    return size / (1024.0 * 1024.0)
}

fun isPassportPhoto(context: Context, uri: Uri): Boolean {
    val inputStream = context.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    inputStream?.close()

    return bitmap?.let {
        val ratio = it.width.toFloat() / it.height.toFloat()
        (ratio in 0.74f..0.76f) || (it.width == 600 && it.height == 800)
    } ?: false
}
