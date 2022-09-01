package com.smic.newsapp

import com.smic.newsapp.model.entities.NewsResponse


/**
 * @author Smogevscih Yuri
31.08.2022
 **/
sealed class NewsUiState {
    object Empty : NewsUiState()
    object Loading : NewsUiState()
    class Loaded(val data: NewsResponse) : NewsUiState()
    class Error(val message: String) : NewsUiState()
}