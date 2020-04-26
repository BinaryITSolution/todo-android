package com.dewan.todoapp.model.repository

import com.dewan.todoapp.model.local.db.AppDatabase
import com.dewan.todoapp.model.local.entity.TaskEntity
import com.dewan.todoapp.model.remote.NetworkService

class TaskRepository(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase
) {

    suspend fun getAllTask(token: String) = networkService.getAllTask(token)

    suspend fun getTaskById(token: String, maxId: String) = networkService.getTAskById(token, maxId)

    suspend fun insert(taskEntity: TaskEntity) = appDatabase.taskDao().insert(taskEntity)

    suspend fun update(taskEntity: TaskEntity) = appDatabase.taskDao().update(taskEntity)

    suspend fun delete(taskEntity: TaskEntity) = appDatabase.taskDao().delete(taskEntity)

    suspend fun getAllTaskFromDb() = appDatabase.taskDao().getAllTaskFromDd()

    suspend fun getMaxId() = appDatabase.taskDao().getMaxTaskId()
}