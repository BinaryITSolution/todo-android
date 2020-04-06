package com.dewan.todoapp.viewmodel.task

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dewan.todoapp.BuildConfig
import com.dewan.todoapp.model.local.AppPreferences
import retrofit2.HttpException

class TaskDetailViewModel : ViewModel() {

    companion object {
        const val TAG = "TaskDetailViewModel"
    }

    private lateinit var sharesPreferences: SharedPreferences
    private lateinit var appPreferences: AppPreferences
    private lateinit var user_id: String

    val id: MutableLiveData<String> = MutableLiveData()
    val dataTime: MutableLiveData<String> = MutableLiveData()
    val title: MutableLiveData<String> = MutableLiveData()
    val body : MutableLiveData<String> = MutableLiveData()
    val status : MutableLiveData<String> = MutableLiveData()
    val userId : MutableLiveData<String> = MutableLiveData()
    val bgColor : MutableLiveData<String> = MutableLiveData()
    val isEditable: MutableLiveData<Boolean> = MutableLiveData()

    fun init(context: Context){

        sharesPreferences = context.getSharedPreferences(BuildConfig.PREF_NAME, Context.MODE_PRIVATE)
        appPreferences = AppPreferences(sharesPreferences)
        user_id = appPreferences.getUserId().toString()

    }

    fun checkUserId(){
        try {
            isEditable.value = userId.value == user_id
        }
        catch (httpException: HttpException){
            Log.e(TAG,httpException.toString())

        }
        catch (exception: Exception){
            Log.e(TAG,exception.toString())
        }

    }
}
