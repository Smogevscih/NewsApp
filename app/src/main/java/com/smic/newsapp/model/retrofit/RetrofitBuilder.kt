package com.smic.newsapp.model.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Smogevscih Yuri
01.09.2022
 **/
object RetrofitBuilder {
    private const val BASE_URL = "https://newsapi.org/v2/"
    val newsApi: NewsApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        newsApi = retrofit.create(NewsApi::class.java)
    }
}