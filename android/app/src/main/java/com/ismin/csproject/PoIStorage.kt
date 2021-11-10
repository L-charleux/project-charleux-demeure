package com.ismin.csproject

import java.text.Normalizer

class PoIStorage {
    private val storage: ArrayList<PoI> = ArrayList() //Stores the PoIs

    /**
     * Add a PoI to the storage if it is not already in it
     */
    fun addPoI(poi: PoI){
        if (!this.storage.contains(poi))
            this.storage.add(poi)
    }

    /**
     * Get all the PoIs from the storage sorted by names
     */
    fun getAllPoIs(): ArrayList<PoI>{
        return ArrayList(
            this.storage.filter{ p: PoI -> p.favorite }.sortedWith(compareBy { stripAccents(it.name.lowercase()) })
                    + this.storage.filter{ p: PoI -> !p.favorite }.sortedWith(compareBy { stripAccents(it.name.lowercase()) }))
    }

    /**
     * Delete all the PoIs in the storage
     */
    fun clear() {
        storage.clear()
    }

    /**
     * Remove all the accents in a String
     */
    private fun stripAccents(s: String): String? {
        var s: String = s
        s = Normalizer.normalize(s, Normalizer.Form.NFD)
        s = s.replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
        return s
    }

}