package com.example.todoapp.view.main

import android.os.Handler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Task
import com.example.todoapp.model.TaskCategory
import com.example.todoapp.model.TaskType
import com.example.todoapp.repo.tasks.TasksRepository
import com.example.todoapp.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tasksRepo: TasksRepository
):ViewModel() {
    private val handler = Handler()
    private val _tasksList = MutableStateFlow<List<Task>>(emptyList())
    val tasksList = _tasksList.asStateFlow()

    private val _selectedFilter = MutableStateFlow("")
    val selectedFilter: StateFlow<String> = _selectedFilter

    val completedTasksList = tasksList.map {
        it.filter { task -> task.isCompleted }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(),emptyList())

    init {
        initData()
    }

    private fun initData() {
        viewModelScope.launch {
            tasksRepo.getAllTasks().collect { tasks ->
                when (_selectedFilter.value) {
                    Constant.UPCOMING_TITLE -> { upcomingClicked() }
                    Constant.PAST_DUE_TITLE -> { pastDueClicked() }
                    else -> {
                        _tasksList.value = tasks
                        _selectedFilter.value=""
                    }
                }
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
                    _selectedFilter.value=""
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
            _selectedFilter.value = Constant.UPCOMING_TITLE
        }
    }

    fun pastDueClicked() {
        viewModelScope.launch {
            val filteredTask = tasksRepo.getTasksWithDueDatePassed()
            _tasksList.value = filteredTask
            _selectedFilter.value = Constant.PAST_DUE_TITLE
        }
    }

    fun allTasksClicked() {
        viewModelScope.launch {
            tasksRepo.getAllTasks().collect{
                _tasksList.value = it
                _selectedFilter.value = Constant.ALL_TASKS_TITLE
            }
        }
    }

    fun handleQueryChange(query: String, onQueryChange: (String) -> Unit, delay: Long) {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            onQueryChange(query)
        }, delay)
    }

    override fun onCleared() {
        super.onCleared()
        _selectedFilter.value=""
        handler.removeCallbacksAndMessages(null)
    }
}