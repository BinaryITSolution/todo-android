package com.dewan.todoapp.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dewan.todoapp.BuildConfig
import com.dewan.todoapp.model.remote.NetworkService
import com.dewan.todoapp.model.remote.Networking
import com.dewan.todoapp.model.remote.request.auth.RegisterRequest
import com.dewan.todoapp.model.repository.RegisterRepository
import kotlinx.coroutines.Dispatchers.IO

class RegisterViewModel : ViewModel() {

    companion object {
        const val TAG = "RegisterViewModel"
    }

    private val networkService = Networking.create(BuildConfig.BASE_URL)
    private val registerRepository = RegisterRepository(networkService)

    fun register(registerRequest: RegisterRequest) = liveData(IO) {

        val data = registerRepository.register(registerRequest)
        emit(data)
    }
}