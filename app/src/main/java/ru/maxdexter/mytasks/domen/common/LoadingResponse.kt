package ru.maxdexter.mytasks.domen.common

sealed class LoadingResponse{
    class Success<T>(val data: T) : LoadingResponse()
    class Error(val message: String) : LoadingResponse()
    object Loading : LoadingResponse()
}
