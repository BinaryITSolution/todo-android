package com.dewan.todoapp.util


/*
created by Richard Dewan 12/08/2020
*/

sealed class ResultState {

    data class Loading(val status: NetworkStatus, val data: Any?, val statusCode: Int = 0) : ResultState()
    data class Success(var status: NetworkStatus,  var data: Any?, val statusCode: Int = 0) : ResultState()
    data class Error(val status: NetworkStatus, val data: Any?, val statusCode: Int = 0) : ResultState()
    data class Unknown(val status: NetworkStatus, val data: Any?, val statusCode: Int = 0) : ResultState()
}

