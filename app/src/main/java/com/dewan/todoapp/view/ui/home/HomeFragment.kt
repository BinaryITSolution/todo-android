package com.dewan.todoapp.view.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.dewan.todoapp.R
import com.dewan.todoapp.databinding.HomeFragmentBinding
import com.dewan.todoapp.model.local.entity.TaskEntity
import com.dewan.todoapp.model.remote.response.todo.TaskResponse
import com.dewan.todoapp.view.adaptor.TaskAdaptor
import com.dewan.todoapp.view.adaptor.TaskCallBack
import com.dewan.todoapp.view.ui.auth.LoginActivity
import com.dewan.todoapp.viewmodel.home.HomeViewModel
import kotlinx.android.synthetic.main.home_fragment.*
import org.jetbrains.anko.support.v4.alert
import timber.log.Timber

class HomeFragment : Fragment(), TaskCallBack {

    companion object {
        const val TAG = "HomeFragment"
    }

    private lateinit var viewModel: HomeViewModel
    private var taskList : ArrayList<TaskEntity> = ArrayList()
    private lateinit var binding: HomeFragmentBinding
    private lateinit var recycleView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var taskAdaptor: TaskAdaptor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.home_fragment,container,false)

        //recycle view
        recycleView = binding.taskRecyclerView
        layoutManager = LinearLayoutManager(context)
        recycleView.layoutManager = layoutManager

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        observer()

    }

    private fun setRecycleView(){
        taskAdaptor = TaskAdaptor(taskList)
        recycleView.adapter = taskAdaptor
        taskAdaptor.setTaskCallBack(this)

    }

    override fun onTaskClick(view: View, position: Int, isLongClick: Boolean) {

        if (isLongClick ){
            Timber.e("Position: $position is a long click")
        }
        else {
            val data = viewModel.taskListFromDb.value?.get(position)
            findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToTaskDetailFragment(
                data?.createdAt.toString(),
                data?.title.toString(),
                data?.body.toString(),
                data?.status.toString(),
                data?.userId.toString(),
                data?.bg_color.toString(),
                data?.id.toString(),
                data?.taskId.toString(),
                data?.note.toString()
            ))
            Timber.e("Position: $position is a single click")
        }
    }

    private fun observer(){
        viewModel.isError.observe(viewLifecycleOwner, Observer {
            errorDialog(it)
        })

        viewModel.progress.observe(viewLifecycleOwner, Observer {
            pb_home.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.taskListFromDb.observe(viewLifecycleOwner, Observer {
            //clear data for our task list
            taskList.clear()
            //add data to our task list
            taskList = it!!.toCollection(taskList)
            //set the recycle view
            setRecycleView()
        })
    }

    private fun errorDialog(errorMsg: String){
        alert {
            title = getString(R.string.title_error_dialog)
            message = errorMsg
            isCancelable = false
            positiveButton(getString(R.string.btn_ok)){dialog->
                dialog.dismiss()
            }
        }.show()

    }


}
