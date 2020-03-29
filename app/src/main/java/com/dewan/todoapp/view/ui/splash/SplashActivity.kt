package com.dewan.todoapp.view.ui.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dewan.todoapp.R
import com.dewan.todoapp.model.local.AppPreferences
import com.dewan.todoapp.util.GeneralHelper
import com.dewan.todoapp.util.NetworkHelper
import com.dewan.todoapp.view.ui.auth.LoginActivity
import com.dewan.todoapp.view.ui.main.MainActivity
import com.dewan.todoapp.viewmodel.splash.SplashViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import org.jetbrains.anko.alert
import org.jetbrains.anko.intentFor

class SplashActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SplashActivity"
    }
    private lateinit var viewModel: SplashViewModel
    private val mContext = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //hide status bar
        GeneralHelper.hideStatusBar(this)

        //view model
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)


        CoroutineScope(Dispatchers.IO).launch {
            checkNetwork()
        }



    }

    suspend fun checkNetwork() {

        delay(2000L)

        val status = NetworkHelper.isNetworkConnected(this)

        if (status) {

            withContext(Main){
                viewModel.token.observe(mContext, Observer {

                    if (it.isNullOrEmpty()){
                        startActivity(intentFor<LoginActivity>())
                    }
                    else {
                        viewModel.validateToken().observe(this@SplashActivity, Observer {

                            if (it.code() == 200) {

                                val msg = it.body()

                                if (msg?.message == "true"){
                                    finish()
                                    startActivity(intentFor<MainActivity>())
                                }
                                else {
                                    startActivity(intentFor<LoginActivity>())
                                }
                            }
                        })
                    }
                })


            }
        } else {

            withContext(Dispatchers.Main){

                showAlertDialog()
            }
        }
    }

    fun  showAlertDialog(){
        alert {
            isCancelable = false
            title = getString(R.string.error_no_internet)
            message = getString(R.string.error_no_internet_msg)
            positiveButton("OK") {
                it.dismiss()
                finish()
            }
        }.show()
    }

    override fun onRestart() {
        super.onRestart()

        CoroutineScope(Dispatchers.IO).launch {
            checkNetwork()
        }
    }
}
