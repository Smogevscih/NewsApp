package com.smic.newsapp

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smic.newsapp.model.common.Result
import com.smic.newsapp.model.entities.Articles
import com.smic.newsapp.model.retrofit.NewsApiHelper
import com.smic.newsapp.model.retrofit.RetrofitBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author Smogevscih Yuri
31.08.2022
 **/
class NewsViewModel : ViewModel() {

    val newsUiState: StateFlow<NewsUiState>
        get() = _newsUiState

    private val _newsUiState = MutableStateFlow<NewsUiState>(NewsUiState.Empty)

    private val newsApiHelper: NewsApiHelper = NewsApiHelper(RetrofitBuilder.newsApi)

    val state = mutableStateOf(
        TextFieldValue()
    )
    lateinit var articles: Articles


    fun loadNews(quest: String) {
        _newsUiState.value = NewsUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = newsApiHelper.getNewsResponse(quest)) {
                is Result.Error -> _newsUiState.value =
                    NewsUiState.Error(result.exception.message ?: "")
                is Result.Success -> _newsUiState.value = NewsUiState.Loaded(result.data)
            }

        }

    }


}


