package com.dewan.todoapp.model.repository

import com.dewan.todoapp.model.remote.NetworkService

class TaskRepository(private val networkService: NetworkService) {

    suspend fun getAllTask(token:String) = networkService.getAllTask(token)
}