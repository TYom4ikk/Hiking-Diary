package com.example.hikingdiary.data.repository

import android.content.Context
import com.example.hikingdiary.data.models.Hike
import java.util.UUID

class HikeRepository(context: Context) {

    private val storage = LocalStorage(context)
    private var hikes = storage.loadHikes()

    fun getAllHikes(): List<Hike> = hikes

    fun addHike(hike: Hike) {
        hikes = hikes + hike
        storage.saveHikes(hikes)
    }

    fun deleteHike(id: String) {
        hikes = hikes.filter { it.id != id }
        storage.saveHikes(hikes)
    }

    fun getById(id: String): Hike? = hikes.find { it.id == id }

    fun createId(): String = UUID.randomUUID().toString()
}