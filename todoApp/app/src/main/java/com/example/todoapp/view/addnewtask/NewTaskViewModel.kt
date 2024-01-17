package com.example.todoapp.view.addnewtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Task
import com.example.todoapp.repo.tasks.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewTaskViewModel @Inject constructor(
    private val tasksRepo: TasksRepository
) : ViewModel() {

    fun addTask(task: Task) = viewModelScope.launch {
        tasksRepo.addNewTask(task)
    }
}
