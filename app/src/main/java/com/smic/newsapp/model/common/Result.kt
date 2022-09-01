package com.smic.newsapp.model.common

/**
 * A generic class that holds a value or an error status.
 * @param <T>
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

}