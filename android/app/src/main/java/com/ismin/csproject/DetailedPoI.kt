package com.ismin.csproject

import java.io.Serializable

data class DetailedPoI(
    val name: String,
    val place: String,
    val latitude: Double,
    val longitude: Double,
    val commentary: String,
    val level: String,
    val type: String,
    val pictureLink: String,
    val favorite: Boolean
): Serializable