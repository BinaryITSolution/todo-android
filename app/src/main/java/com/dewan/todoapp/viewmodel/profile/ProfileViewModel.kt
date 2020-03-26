package com.dewan.todoapp.viewmodel.profile

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dewan.todoapp.BuildConfig
import com.dewan.todoapp.model.local.AppPreferences
import com.dewan.todoapp.model.remote.Networking
import com.dewan.todoapp.model.remote.response.profile.UserProfileResponse
import com.dewan.todoapp.model.repository.UserProfileRepository
import com.dewan.todoapp.util.NetworkHelper

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG = "ProfileViewModel"
    }

    private val networkService = Networking.create(BuildConfig.BASE_URL)
    private lateinit var userProfileRepository: UserProfileRepository
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var appPreferences: AppPreferences
    private lateinit var token: String
    private lateinit var userId: String
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var profile: UserProfileResponse
    val imageUrl: MutableLiveData<String> = MutableLiveData()


    init {
        userProfileRepository = UserProfileRepository(networkService)
        sharedPreferences = application.getSharedPreferences("com.dewan.todoapp.pref", Context.MODE_PRIVATE)
        appPreferences = AppPreferences(sharedPreferences)
        token = appPreferences.getAccessToken().toString()
        userId = appPreferences.getUserId().toString()
    }

    fun getUserProfile() = liveData {
        loading.postValue(true)
        val data = userProfileRepository.getUserProfile(token,userId)
        if (data.code() == 200 ){
            profile = data.body()!!

            imageUrl.postValue(profile.profileImage)
        }

        emit(profile)
        loading.postValue(false)
    }

}
