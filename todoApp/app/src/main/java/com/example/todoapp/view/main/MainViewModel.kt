package com.example.todoapp.view.main

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Task
import com.example.todoapp.model.TaskCategory
import com.example.todoapp.model.TaskType
import com.example.todoapp.repo.tasks.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val tasksRepo: TasksRepository
):ViewModel() {
    private val _tasksList = MutableStateFlow<List<Task>>(emptyList())
    val tasksList = _tasksList.asStateFlow()

    val completedTasksList = tasksList.map {
        it.filter { task -> task.isCompleted }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(),emptyList())

    init {
        initData()
        Log.d("main","init view model: $this ")
    }

    private fun initData() {
        viewModelScope.launch {
            tasksRepo.getAllTasks().collect { tasks ->
                _tasksList.value = tasks
            }
        }
    }

    fun onCompletedChanged(task: Task) = viewModelScope.launch {
        val taskCopy = task.copy(isCompleted = !(task.isCompleted))
        tasksRepo.updateTask(taskCopy)
    }

    fun deleteTask(task: Task)=viewModelScope.launch {
        tasksRepo.deleteTask(task)
    }

    fun updateTaskListWithQuery(query: String?) {
        viewModelScope.launch {
            tasksRepo.getAllTasks()
                .onEach { posts ->
                    val filteredTask = if (query.isNullOrBlank()) {
                        posts
                    } else {
                        posts.filter { post ->
                            post.title.contains(query, ignoreCase = true)
                        }
                    }
                    _tasksList.value = filteredTask
                }
                .collect()
        }
    }

    fun retrieveTaskCategories(tasks:List<Task>):List<TaskCategory>{
        val tasksCategory = mutableListOf<TaskCategory>()

        val urgentTasks = tasks.filter { it.urgent }
        if (urgentTasks.isNotEmpty()) {
            tasksCategory.add(TaskCategory(TaskType.ARGENT.displayName,urgentTasks))
        }

        val othersTasks = tasks.filter { !it.urgent }
        if (othersTasks.isNotEmpty()) {
            tasksCategory.add(TaskCategory(TaskType.OTHERS.displayName,othersTasks))
        }
        return tasksCategory
    }

    fun upcomingClicked() {
        viewModelScope.launch {
            val filteredTask = tasksRepo.getTasksWithDueDateUpcoming()
            _tasksList.value = filteredTask
        }
    }

    fun pastDueClicked() {
        viewModelScope.launch {
            Log.d("main","viewmodel")
            val filteredTask = tasksRepo.getTasksWithDueDatePassed()
            _tasksList.value = filteredTask
        }
    }

    fun allTasksClicked() {
        viewModelScope.launch {
            tasksRepo.getAllTasks().collect{
                _tasksList.value = it
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("main","onCleared: $this")
    }
}