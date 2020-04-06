package com.dewan.todoapp.viewmodel.home

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
import com.dewan.todoapp.model.remote.response.todo.TaskResponse
import com.dewan.todoapp.model.repository.TaskRepository
import retrofit2.HttpException

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "HomeViewModel"
    }

    private val networkService = Networking.create(BuildConfig.BASE_URL)
    private var taskRepository: TaskRepository
    private var sharesPreferences = application.getSharedPreferences(BuildConfig.PREF_NAME,Context.MODE_PRIVATE)
    private var appPreferences: AppPreferences
    private var token: String
    val taskList: MutableLiveData<List<TaskResponse>> = MutableLiveData()
    val progress: MutableLiveData<Boolean> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()


    init {
        taskRepository = TaskRepository(networkService)
        appPreferences = AppPreferences(sharesPreferences)
        token = appPreferences.getAccessToken().toString()
    }


    fun getAllTask() = liveData {
        try {
            progress.postValue(true)
            val data = taskRepository.getAllTask(token)

            if (data.code() == 200){
                taskList.value = data.body()
            }
            emit(taskList.value)

            progress.postValue(false)

        }
        catch (httpException: HttpException){
            Log.e(TAG,httpException.toString())
            isError.value = httpException.toString()

        }
        catch (exception: Exception){
            Log.e(TAG,exception.toString())
            isError.value = exception.toString()
        }

    }

}
