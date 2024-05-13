package com.batsworks.budget.components

sealed class Resource<out R> {
    data class Sucess<out R>(val result: R) : Resource<R>()
    data class Failure(val error: String? = null) : Resource<Nothing>()
    data class Loading(val loading: Boolean = true) : Resource<Nothing>()
}
