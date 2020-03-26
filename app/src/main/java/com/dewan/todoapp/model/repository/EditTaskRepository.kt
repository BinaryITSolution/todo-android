package com.dewan.todoapp.model.repository

import com.dewan.todoapp.model.remote.NetworkService
import com.dewan.todoapp.model.remote.request.todo.EditTaskRequest
import com.dewan.todoapp.model.remote.response.todo.EditTaskResponse
import retrofit2.Response

class EditTaskRepository(private val networkService: NetworkService) {

    suspend fun editTak(token: String, editTaskRequest: EditTaskRequest): Response<EditTaskResponse> =
        networkService.editTask(token, editTaskRequest )

}