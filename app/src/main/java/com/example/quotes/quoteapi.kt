package com.example.quotes
import retrofit2.http.GET
import retrofit2.Response


interface quoteapi {
    @GET("random")
    suspend fun getquote() : Response<List<quotemodel>>
}