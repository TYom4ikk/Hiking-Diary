package com.example.hikingdiary.data.models

data class Hike(
    val id: String,
    val title: String,
    val date: String,
    val location: String,
    val description: String,
    val photos: List<Photo>
)