package com.dewan.todoapp.viewmodel.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dewan.todoapp.BuildConfig
import com.dewan.todoapp.model.remote.NetworkService
import com.dewan.todoapp.model.remote.Networking
import com.dewan.todoapp.model.remote.request.auth.RegisterRequest
import com.dewan.todoapp.model.repository.RegisterRepository
import kotlinx.coroutines.Dispatchers.IO
import retrofit2.HttpException

class RegisterViewModel : ViewModel() {

    companion object {
        const val TAG = "RegisterViewModel"
    }

    private val networkService = Networking.create(BuildConfig.BASE_URL)
    private val registerRepository = RegisterRepository(networkService)

    fun register(registerRequest: RegisterRequest) = liveData(IO) {
        try {
            val data = registerRepository.register(registerRequest)
            emit(data)

        } catch (httpException: HttpException) {
            Log.e(TAG, httpException.toString())

        } catch (exception: Exception) {
            Log.e(TAG, exception.toString())
        }

    }
}