package com.ismin.csproject

import java.io.Serializable

data class PoI(
    var name: String,
    var place: String,
    var latitude: Double,
    var longitude: Double,
    var level: String,
    var type: String,
    var favorite: Boolean
    ): Serializable