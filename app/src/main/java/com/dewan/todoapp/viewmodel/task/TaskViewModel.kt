package com.dewan.todoapp.viewmodel.task

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dewan.todoapp.BuildConfig
import com.dewan.todoapp.model.local.AppPreferences
import com.dewan.todoapp.model.remote.Networking
import com.dewan.todoapp.model.remote.request.todo.AddTaskRequest
import com.dewan.todoapp.model.repository.AddTaskRepository
import retrofit2.HttpException


class TaskViewModel : ViewModel() {

    companion object {
        const val TAG = "TaskViewModel"
    }

    private val networkService = Networking.create(BuildConfig.BASE_URL)
    private lateinit var addTaskRepository: AddTaskRepository
    private lateinit var sharesPreferences: SharedPreferences
    private lateinit var appPreferences: AppPreferences
    private var token: String = ""
    val user_id: MutableLiveData<Int> = MutableLiveData()
    val progress: MutableLiveData<Boolean> = MutableLiveData()


    fun init(context: Context){
        addTaskRepository = AddTaskRepository(networkService)
        sharesPreferences = context.getSharedPreferences("com.dewan.todoapp.pref",Context.MODE_PRIVATE)
        appPreferences = AppPreferences(sharesPreferences)
        token = appPreferences.getAccessToken().toString()
        user_id.value = appPreferences.getUserId()

    }

    fun addTask(addTaskRequest: AddTaskRequest) = liveData {
        try {
            progress.value = true

            val data =  addTaskRepository.addTask(token,addTaskRequest)
            emit(data)

            progress.value = false


        }
        catch (httpException: HttpException){
            Log.e(TAG,httpException.toString())

        }
        catch (exception: Exception){
            Log.e(TAG,exception.toString())
        }


    }
}
