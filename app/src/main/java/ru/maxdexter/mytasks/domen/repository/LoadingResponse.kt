package ru.maxdexter.mytasks.domen.repository

sealed class LoadingResponse<T>{
    class Success<T>(val data: T ,val flag: Boolean) : LoadingResponse<T>()
    class Error<T>(val message: T) : LoadingResponse<T>()
    class Loading<T>: LoadingResponse<T>()
}
