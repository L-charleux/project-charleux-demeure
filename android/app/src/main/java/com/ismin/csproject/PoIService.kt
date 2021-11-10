package com.ismin.csproject

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface PoIService {

    /**
     * Used to get all the dataset of PoIs from the API
     */
    @GET("PoIs")
    fun getAllPoIs(): Call<List<PoI>>

    /**
     * Used to get the details of a PoI from the API
     */
    @GET("PoIs/{latitude}/{longitude}")
    fun getDetailedPoI(@Path("latitude") latitude: String, @Path("longitude") longitude: String): Call<DetailedPoI>

    /**
     * Used to toggle the favorite parameter of a PoI on the API
     */
    @PUT("PoIs/{latitude}/{longitude}")
    fun putFavoritePoI(@Path("latitude") latitude: String, @Path("longitude") longitude: String): Call<PoI>
}