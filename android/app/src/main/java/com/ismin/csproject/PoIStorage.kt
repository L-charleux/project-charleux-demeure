package com.ismin.csproject

import org.apache.commons.lang3.StringUtils
import java.text.Normalizer

class PoIStorage {
    private val storage: ArrayList<PoI> = ArrayList()

    fun addPoI(poi: PoI){
        if (!this.storage.contains(poi))
            this.storage.add(poi)
    }

    fun getPoI(latitude: Double, longitude: Double): PoI?{
        return this.storage.find { p: PoI -> p.latitude == latitude && p.longitude == longitude }
    }

    fun getPoIsOf(place: String): List<PoI> {
        return this.storage.filter { p: PoI -> p.place == place }.sortedWith(compareBy { it.name })
    }

    fun getAllPoIs(): ArrayList<PoI>{
        return ArrayList(this.storage.filter{ p: PoI -> p.favorite }.sortedWith(compareBy { stripAccents(it.name.lowercase()) }) + this.storage.filter{ p: PoI -> !p.favorite }.sortedWith(compareBy { stripAccents(it.name.lowercase()) }))
    }

    fun getTotalNumberOfPoIs() : Number {
        return this.storage.size
    }

    fun clear() {
        storage.clear()
    }

    fun stripAccents(s: String): String? {
        var s = s
        s = Normalizer.normalize(s, Normalizer.Form.NFD)
        s = s.replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
        return s
    }

}