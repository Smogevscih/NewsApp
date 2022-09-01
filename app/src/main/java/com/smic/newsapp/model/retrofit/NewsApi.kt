package com.smic.newsapp.model.retrofit

import com.smic.newsapp.getDateAgo
import com.smic.newsapp.model.entities.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Smogevscih Yuri
31.08.2022
 **/
interface NewsApi {
    /*
    everything?q=tesla&from=2022-07-30&sortBy=publishedAt&apiKey=390c10759ef54f4b8277fad315bcc9aa
     */

    @GET("everything?sortBy=publishedAt&apiKey=390c10759ef54f4b8277fad315bcc9aa")
    fun getNews(@Query("q") q: String, @Query("from") from: String= getDateAgo(30)): Call<NewsResponse>

}