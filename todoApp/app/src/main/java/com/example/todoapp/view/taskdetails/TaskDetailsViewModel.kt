package com.example.todoapp.view.taskdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Task
import com.example.todoapp.repo.tasks.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val tasksRepo: TasksRepository
): ViewModel() {
    private val _state = MutableStateFlow<Task?>(null)
    val state = _state.asStateFlow()

    fun getTask(taskId:Int) = viewModelScope.launch {
        val task = tasksRepo.getTaskById(taskId)
        _state.value = task
    }
}