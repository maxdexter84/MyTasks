package ru.maxdexter.mytasks.domen.repository

sealed class LoadingResponse{
    class Success<T>(val data: T ,val flag: Boolean) : LoadingResponse()
    class Error(val message: String, val flag: Boolean) : LoadingResponse()
    object Loading : LoadingResponse()
}