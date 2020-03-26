package com.dewan.todoapp.viewmodel.task

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dewan.todoapp.BuildConfig
import com.dewan.todoapp.model.local.AppPreferences
import com.dewan.todoapp.model.remote.Networking
import com.dewan.todoapp.model.remote.request.todo.EditTaskRequest
import com.dewan.todoapp.model.repository.AddTaskRepository
import com.dewan.todoapp.model.repository.EditTaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditTaskViewModel : ViewModel() {

    companion object {
        const val TASK = "EditTaskViewModel"
    }

    private val networkService = Networking.create(BuildConfig.BASE_URL)
    private lateinit var editTaskRepository: EditTaskRepository
    private lateinit var sharesPreferences: SharedPreferences
    private lateinit var appPreferences: AppPreferences
    private var token: String = ""
    val user_id: MutableLiveData<Int> = MutableLiveData()

    val id: MutableLiveData<String> = MutableLiveData()
    val title: MutableLiveData<String> = MutableLiveData()
    val body: MutableLiveData<String> = MutableLiveData()
    val status: MutableLiveData<String> = MutableLiveData()
    val index: MutableLiveData<Int> = MutableLiveData()
    val taskList: ArrayList<String> = ArrayList()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()

    fun init(context: Context){
        editTaskRepository = EditTaskRepository(networkService)
        sharesPreferences = context.getSharedPreferences("com.dewan.todoapp.pref", Context.MODE_PRIVATE)
        appPreferences = AppPreferences(sharesPreferences)
        token = appPreferences.getAccessToken().toString()
        user_id.value = appPreferences.getUserId()

    }

    fun getIndexFromTaskList(){
        index.value = taskList.indexOf(status.value)
    }

    fun editTask() {
        CoroutineScope(Dispatchers.IO).launch {
            loading.postValue(true)
            val data = editTaskRepository.editTak(token, EditTaskRequest(
                id.value!!.toInt(),
                user_id.value.toString(),
                title.value.toString(),
                body.value.toString(),
                status.value.toString()
            ))
            if (data.code() == 201){
                isSuccess.postValue(true)
            }
            else {
                isSuccess.postValue(false)
            }

            loading.postValue(false)
        }

    }



}
