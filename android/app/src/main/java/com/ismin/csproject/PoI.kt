package com.ismin.csproject

import java.io.Serializable

data class PoI(
    val name: String,
    val place: String,
    val latitude: Double,
    val longitude: Double,
    val level: String,
    val type: String,
    val favorite: Boolean
    ): Serializable