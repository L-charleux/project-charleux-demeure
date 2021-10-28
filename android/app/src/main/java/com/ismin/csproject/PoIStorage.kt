package com.ismin.csproject

class PoIStorage {
    private val storage: ArrayList<PoI> = ArrayList()

    fun addPoI(poi: PoI){
        if (!this.storage.contains(poi))
            this.storage.add(poi)
    }

    fun getPoI(longitude: Double, latitude: Double): PoI?{
        return this.storage.find { p: PoI -> p.longitude == longitude && p.latitude == latitude }
    }

    fun getPoIsOf(place: String): List<PoI> {
        return this.storage.filter { p: PoI -> p.place == place }.sortedWith(compareBy { it.name })
    }

    fun getAllPoIs(): ArrayList<PoI>{
        return ArrayList(this.storage.sortedWith(compareBy { it.name }))
    }

    fun getTotalNumberOfPoIs() : Number {
        return this.storage.size
    }

    fun clear() {
        storage.clear()
    }
}