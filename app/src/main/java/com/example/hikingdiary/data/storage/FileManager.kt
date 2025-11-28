package com.example.hikingdiary.data.storage

import android.content.Context
import java.io.File

class FileManager(private val context: Context) {

    fun writeToFile(fileName: String, content: String) {
        val file = File(context.filesDir, fileName)
        file.writeText(content)
    }

    fun readFromFile(fileName: String): String? {
        val file = File(context.filesDir, fileName)
        return if (file.exists()) file.readText() else null
    }
}