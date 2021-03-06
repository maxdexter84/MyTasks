package ru.maxdexter.mytasks.domen.models

sealed class  DataState<T> {
    class Load<T>(): DataState<T>()
    class Upload<T>(): DataState<T>()
    class Success<T>(val data : T): DataState<T>()
    class Error<T>(val error : T): DataState<T>()

}