package com.ismin.csproject

import java.io.Serializable

data class PoI(
    val name: String,
    val place: String,
    val latitude: Double,
    val longitude: Double,
    val badge: Boolean,
    val commentary: String,
    val level: String,
    val type: String,
    val pictureLink: String
    ): Serializable