package com.smic.newsapp.model.retrofit

import com.smic.newsapp.model.common.Result
import com.smic.newsapp.model.entities.NewsResponse
import com.smic.newsapp.model.retrofit.NewsApi

/**
 * @author Smogevscih Yuri
01.09.2022
 **/
class NewsApiHelper(private val newsApi: NewsApi) {


    fun getNewsResponse(quest: String): Result<NewsResponse> {
        val request = newsApi.getNews(quest)
        return try {
            val response = request.execute()
            val newsResponse = response.body()
            if (newsResponse == null) throw Exception("Empty News response") else
                Result.Success(newsResponse)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}