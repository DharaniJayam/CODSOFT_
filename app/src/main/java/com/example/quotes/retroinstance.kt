package com.example.quotes

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object retroinstance {
    private const val BASE_URL = "https://zenquotes.io/api/"

    private fun getinstance() : Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val quotesapi : quoteapi= getinstance().create(quoteapi::class.java)
}