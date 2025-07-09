package com.example.equipocuatro.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.equipocuatro.utils.Constants.BASE_URL

object RetrofitClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val consumeApi= retrofit.create(ConsumeApi::class.java)

}