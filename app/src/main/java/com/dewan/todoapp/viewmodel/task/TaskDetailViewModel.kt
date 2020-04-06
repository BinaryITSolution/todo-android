package com.dewan.todoapp.viewmodel.task

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dewan.todoapp.BuildConfig
import com.dewan.todoapp.model.local.AppPreferences
import retrofit2.HttpException

class TaskDetailViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "TaskDetailViewModel"
    }


    private var sharesPreferences = application.getSharedPreferences(BuildConfig.PREF_NAME, Context.MODE_PRIVATE)
    private lateinit var appPreferences: AppPreferences
    private lateinit var user_id: String


    val id: MutableLiveData<String> = MutableLiveData()
    val dataTime: MutableLiveData<String> = MutableLiveData()
    val title: MutableLiveData<String> = MutableLiveData()
    val body : MutableLiveData<String> = MutableLiveData()
    val status : MutableLiveData<String> = MutableLiveData()
    val userIdField : MutableLiveData<String> = MutableLiveData()
    val bgColor : MutableLiveData<String> = MutableLiveData()
    val isEditable: MutableLiveData<Boolean> = MutableLiveData()


    init{
        appPreferences = AppPreferences(sharesPreferences)
        user_id = appPreferences.getUserId().toString()



    fun checkUserId(){
        try {
            isEditable.value = userIdField.value == userId
        }
        catch (httpException: HttpException){
            Log.e(TAG,httpException.toString())

        }
        catch (exception: Exception){
            Log.e(TAG,exception.toString())
        }

    }
}
