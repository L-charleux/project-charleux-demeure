package com.ismin.csproject

import retrofit2.Call
//import retrofit2.http.Body
import retrofit2.http.GET
//import retrofit2.http.POST

interface PoIService {

    @GET("PoIs")
    fun getAllPoIs(): Call<List<PoI>>

    /*
    @POST("PoIs")
    fun createPoI(@Body poi: PoI): Call<PoI>
     */
}