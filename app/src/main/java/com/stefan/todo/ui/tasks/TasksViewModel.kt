package com.stefan.todo.ui.tasks

import androidx.lifecycle.ViewModel
import com.stefan.todo.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveData.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.stefan.todo.data.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

@HiltViewModel
class TasksViewModel  @Inject constructor(
    private val taskDao: TaskDao
) : ViewModel() {
    val searchQuery = MutableStateFlow("")
    private val taskFlow = searchQuery.flatMapLatest {
        taskDao.getTasks(it)
    }
    val tasks = taskFlow.asLiveData()


}