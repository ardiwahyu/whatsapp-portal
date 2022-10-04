package com.gitlab.whatsappportal.data

sealed class Resource<out R> {
    class OnLoading<T>: Resource<T>()

    data class OnSuccess<T>(
        val data: T,
        val message: String
    ): Resource<T>()

    data class OnError<T>(
        val message: String
    ): Resource<T>()
}