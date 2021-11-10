package com.ismin.csproject

import retrofit2.Call
//import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

//import retrofit2.http.POST

interface PoIService {

    @GET("PoIs")
    fun getAllPoIs(): Call<List<PoI>>

    @GET("PoIs/{latitude}/{longitude}")
    fun getDetailedPoI(@Path("latitude") latitude: String, @Path("longitude") longitude: String): Call<DetailedPoI>

    @PUT("PoIs/{latitude}/{longitude}")
    fun putFavoritePoI(@Path("latitude") latitude: String, @Path("longitude") longitude: String): Call<PoI>

    /*
    @POST("PoIs")
    fun createPoI(@Body poi: PoI): Call<PoI>
     */
}