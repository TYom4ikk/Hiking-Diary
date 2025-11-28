package com.example.hikingdiary.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.hikingdiary.data.models.Hike
import com.example.hikingdiary.data.storage.FileManager

class LocalStorage(context: Context) {

    private val gson = Gson()
    private val fileManager = FileManager(context)
    private val fileName = "hikes.json"

    fun saveHikes(hikes: List<Hike>) {
        val json = gson.toJson(hikes)
        fileManager.writeToFile(fileName, json)
    }

    fun loadHikes(): List<Hike> {
        val json = fileManager.readFromFile(fileName) ?: return emptyList()
        val type = object : TypeToken<List<Hike>>() {}.type
        return gson.fromJson(json, type)
    }
}