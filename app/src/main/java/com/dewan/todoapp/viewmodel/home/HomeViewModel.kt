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
import com.dewan.todoapp.model.local.db.AppDatabase
import com.dewan.todoapp.model.local.entity.TaskEntity
import com.dewan.todoapp.model.remote.Networking
import com.dewan.todoapp.model.remote.response.todo.TaskResponse
import com.dewan.todoapp.model.repository.TaskRepository
import com.dewan.todoapp.util.network.NetworkHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import kotlin.math.max

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "HomeViewModel"
    }

    private val networkService = Networking.create(BuildConfig.BASE_URL)
    private var taskRepository: TaskRepository
    private var sharesPreferences =
        application.getSharedPreferences(BuildConfig.PREF_NAME, Context.MODE_PRIVATE)
    private var appPreferences: AppPreferences
    private var token: String
    private val taskList: MutableLiveData<List<TaskResponse>> = MutableLiveData()
    val taskListFromDb: MutableLiveData<List<TaskEntity>> = MutableLiveData()
    val progress: MutableLiveData<Boolean> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()
    private val maxRecId: MutableLiveData<String> = MutableLiveData()
    private var context: Context? = null


    init {

        taskRepository = TaskRepository(networkService, AppDatabase.getInstance(application))
        appPreferences = AppPreferences(sharesPreferences)
        token = appPreferences.getAccessToken().toString()
        context = application

        /*

         */
        getMaxIdFromDb()
    }


    fun getAllTask() = liveData {
        try {
            progress.postValue(true)
            val data = taskRepository.getAllTask(token)

            if (data.code() == 200) {
                taskList.value = data.body()

                /*
                insert task list to local database
                 */
                for (task in taskList.value!!) {

                    val id = taskRepository.insert(
                        TaskEntity(
                            taskId = task.id,
                            title = task.title,
                            body = task.body,
                            status = task.status,
                            userId = task.userId,
                            createdAt = task.createdAt,
                            updatedAt = task.updatedAt
                        )
                    )
                    Log.d(TAG, "New record inserted to local db. RowId: $id")
                }

            }
            emit(taskList.value)

            progress.postValue(false)

        } catch (httpException: HttpException) {
            Log.e(TAG, httpException.toString())
            isError.value = httpException.toString()

        } catch (exception: Exception) {
            Log.e(TAG, exception.toString())
            isError.value = exception.toString()
        }

    }

    private fun getMaxIdFromDb() {
        CoroutineScope(Dispatchers.IO).launch {

            try {

                /*
                get the max rec id from db
                 */
                var maxId = taskRepository.getMaxId()
                //if maxid is null then we set to 0
                if (maxId == null) {
                    maxId = 0
                }

                /*
                get the task y id from API
                 */
                getTaskById(maxId.toString())

                /*
                set the max rec id value
                 */
                maxRecId.postValue(maxId.toString())

            } catch (error: Exception) {
                Log.e(TAG, error.message.toString())
            }
        }
    }

    /*
     get the task y id from API
    */
    private fun getTaskById(maxId: String) {

        try {
            if (NetworkHelper.isNetworkConnected(context!!)){

                CoroutineScope(Dispatchers.Main).launch {

                    progress.postValue(true)

                    val data = taskRepository.getTaskById(token, maxId)

                    if (data.code() == 200) {
                        taskList.value = data.body()

                        /*
                        insert task list to local database
                         */
                        for (task in taskList.value!!) {

                            val id = taskRepository.insert(
                                TaskEntity(
                                    taskId = task.id,
                                    title = task.title,
                                    body = task.body,
                                    note = task.note,
                                    status = task.status,
                                    userId = task.userId,
                                    createdAt = task.createdAt,
                                    updatedAt = task.updatedAt
                                )
                            )
                            Log.d(TAG, "New record inserted to local db. RowId: $id")
                        }

                        /*
                        get the task from db
                         */
                        getTaskFromDb()

                    }

                    progress.postValue(false)
                }
            }
            else {
                /*
                   get the task from db
                */
                getTaskFromDb()

            }

        } catch (error: Exception) {
            Log.e(TAG, error.message.toString())
        }

    }

    /*
    get the task from local db
     */
    private fun getTaskFromDb(){

        try {

            CoroutineScope(Dispatchers.IO).launch {

                val data = taskRepository.getAllTaskFromDb()

                taskListFromDb.postValue(data)
            }

        }catch (error: Exception){
            Log.e(TAG, error.message.toString())
        }
    }



}
